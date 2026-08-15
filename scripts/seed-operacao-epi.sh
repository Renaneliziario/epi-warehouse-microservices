#!/bin/bash
# ============================================================
# seed-operacao-epi.sh
#
# Popula os vinculos operacao<->EPI que nao podem vir do
# Flyway do servico-operacao, porque os epiId reais so
# existem depois de cadastrados no servico-retirada-epi
# (cada servico tem seu proprio gerador de id).
#
# Fase 1: cadastra as categorias de EPI (servico-categoria-epi)
# Fase 2: cadastra os EPIs (servico-retirada-epi)
# Fase 3: vincula os EPIs as operacoes ja semeadas pelo Flyway
#         (servico-operacao)
#
# Pre-requisito: docker compose up --build -d (os 5 servicos
# + postgres precisam estar de pe e saudaveis).
# ============================================================
set -euo pipefail

CATEGORIA_URL="http://localhost:8082"
RETIRADA_URL="http://localhost:8083"
OPERACAO_URL="http://localhost:8084"

# CA (Certificado de Aprovacao) real nao cadastrado ainda neste seed,
# usa a URL publica de consulta do MTE como placeholder honesto.
DOCUMENT_URL="https://consultaca.mte.gov.br/#/"
ESTOQUE_INICIAL=100

declare -A CATEGORIA_ID
declare -A EPI_ID
declare -A OPERACAO_ID

post_json() {
  local url="$1"
  local payload="$2"
  curl -s -X POST "$url" -H "Content-Type: application/json" -d "$payload"
}

echo "== Fase 1: categorias de EPI =="
categorias=(
  "Proteção da Cabeça|Proteção da cabeça contra impactos e queda de objetos"
  "Proteção dos Olhos|Proteção dos olhos contra partículas e impactos"
  "Proteção da Face|Proteção da face contra partículas, respingos e radiação"
  "Proteção Auditiva|Proteção contra níveis elevados de pressão sonora"
  "Proteção das Mãos|Proteção das mãos contra calor, corte, abrasão e produtos químicos"
  "Proteção dos Pés|Proteção dos pés contra impactos e esmagamentos"
  "Proteção do Corpo|Proteção do tronco, pernas e braços contra calor e respingos"
  "Proteção Respiratória|Proteção contra partículas, poeiras, vapores e gases"
  "Proteção Contra Quedas|Sistemas de retenção e ancoragem contra quedas de altura"
  "Proteção Térmica|Proteção contra calor radiante intenso"
  "Alta Visibilidade|Aumenta a visibilidade do trabalhador em áreas de circulação"
)
for entry in "${categorias[@]}"; do
  IFS='|' read -r nome desc <<< "$entry"
  id=$(post_json "$CATEGORIA_URL/epi-categories" "$(jq -n --arg n "$nome" --arg d "$desc" '{name:$n, description:$d}')" | jq -r '.id')
  CATEGORIA_ID["$nome"]="$id"
  echo "  categoria '$nome' -> id $id"
done

echo "== Fase 2: EPIs =="
# nome|descricao|categoria
epis=(
  "Capacete de Segurança|Proteção da cabeça contra impactos e queda de objetos|Proteção da Cabeça"
  "Óculos de Segurança|Proteção dos olhos contra partículas e impactos|Proteção dos Olhos"
  "Protetor Facial|Proteção da face contra partículas, respingos e projeções|Proteção da Face"
  "Protetor Auricular Plug|Proteção auditiva do tipo inserção|Proteção Auditiva"
  "Protetor Auricular Concha|Proteção auditiva do tipo concha|Proteção Auditiva"
  "Luva de Raspa|Proteção das mãos contra calor, abrasão e respingos|Proteção das Mãos"
  "Luva Anticorte|Proteção das mãos contra cortes e materiais cortantes|Proteção das Mãos"
  "Luva Térmica|Proteção das mãos contra altas temperaturas|Proteção das Mãos"
  "Luva Química|Proteção das mãos contra determinados produtos químicos|Proteção das Mãos"
  "Botina de Segurança|Proteção dos pés contra impactos e esmagamentos|Proteção dos Pés"
  "Bota de Segurança|Proteção dos pés em ambientes industriais|Proteção dos Pés"
  "Avental de Raspa|Proteção do tronco contra calor e respingos|Proteção do Corpo"
  "Perneira de Raspa|Proteção das pernas contra calor e respingos|Proteção do Corpo"
  "Mangote|Proteção dos braços contra calor, cortes e abrasão|Proteção do Corpo"
  "Respirador PFF2|Proteção respiratória contra partículas e poeiras|Proteção Respiratória"
  "Respirador com Filtro Químico|Proteção respiratória contra vapores e gases|Proteção Respiratória"
  "Máscara de Solda|Proteção contra radiação, partículas e respingos da soldagem|Proteção da Face"
  "Cinturão Paraquedista|Equipamento usado em sistemas de proteção contra quedas|Proteção Contra Quedas"
  "Talabarte de Segurança|Sistema de conexão em sistemas de proteção contra quedas|Proteção Contra Quedas"
  "Trava-Quedas|Sistema destinado à retenção de quedas|Proteção Contra Quedas"
  "Vestimenta Térmica|Proteção do corpo contra altas temperaturas|Proteção Térmica"
  "Vestimenta Aluminizada|Proteção contra calor radiante intenso|Proteção Térmica"
  "Colete Refletivo|Aumenta a visibilidade em áreas de circulação de cargas|Alta Visibilidade"
)
for entry in "${epis[@]}"; do
  IFS='|' read -r nome desc categoria <<< "$entry"
  catId="${CATEGORIA_ID[$categoria]}"
  payload=$(jq -n --arg name "$nome" --arg desc "$desc" --arg url "$DOCUMENT_URL" \
    --argjson catId "$catId" --argjson stock "$ESTOQUE_INICIAL" \
    '{name:$name, description:$desc, documentUrl:$url, epiCategoryId:$catId, currentStock:$stock}')
  id=$(post_json "$RETIRADA_URL/epis" "$payload" | jq -r '.id')
  EPI_ID["$nome"]="$id"
  echo "  EPI '$nome' -> id $id"
done

echo "== Fase 3: vinculo operacao x EPI =="
operacoes_json=$(curl -s "$OPERACAO_URL/operacoes")
mapfile -t nomes_operacoes < <(echo "$operacoes_json" | jq -r '.[].nome')
for nome in "${nomes_operacoes[@]}"; do
  id=$(echo "$operacoes_json" | jq -r --arg n "$nome" '.[] | select(.nome == $n) | .id')
  OPERACAO_ID["$nome"]="$id"
done

vincular() {
  local operacao="$1"
  shift
  local opId="${OPERACAO_ID[$operacao]:-}"
  if [ -z "$opId" ]; then
    echo "  [aviso] operacao '$operacao' nao encontrada, pulando"
    return
  fi
  for epiNome in "$@"; do
    epiId="${EPI_ID[$epiNome]:-}"
    if [ -z "$epiId" ]; then
      echo "  [aviso] EPI '$epiNome' nao encontrado, pulando"
      continue
    fi
    payload=$(jq -n --argjson epiId "$epiId" '{epiId:$epiId, obrigatorio:true}')
    post_json "$OPERACAO_URL/operacoes/$opId/epis" "$payload" > /dev/null
    echo "  '$operacao' -> '$epiNome'"
  done
}

vincular "Recebimento de matéria-prima" "Capacete de Segurança" "Óculos de Segurança" "Botina de Segurança" "Luva Anticorte"
vincular "Movimentação com ponte rolante" "Capacete de Segurança" "Óculos de Segurança" "Botina de Segurança" "Luva Anticorte"
vincular "Operação de empilhadeira" "Capacete de Segurança" "Óculos de Segurança" "Botina de Segurança" "Colete Refletivo"
vincular "Corte de chapas e perfis" "Óculos de Segurança" "Protetor Facial" "Protetor Auricular Concha" "Luva Anticorte" "Botina de Segurança"
vincular "Esmerilhamento" "Óculos de Segurança" "Protetor Facial" "Protetor Auricular Concha" "Luva de Raspa" "Botina de Segurança"
vincular "Soldagem MIG/MAG" "Máscara de Solda" "Luva de Raspa" "Avental de Raspa" "Perneira de Raspa" "Botina de Segurança" "Respirador PFF2" "Protetor Auricular Concha"
vincular "Soldagem TIG" "Máscara de Solda" "Luva de Raspa" "Avental de Raspa" "Perneira de Raspa" "Botina de Segurança" "Respirador PFF2"
vincular "Soldagem com eletrodo revestido" "Máscara de Solda" "Luva de Raspa" "Avental de Raspa" "Perneira de Raspa" "Botina de Segurança" "Respirador PFF2" "Protetor Auricular Concha"
vincular "Oxicorte" "Óculos de Segurança" "Protetor Facial" "Luva de Raspa" "Avental de Raspa" "Perneira de Raspa" "Botina de Segurança"
vincular "Corte a plasma" "Óculos de Segurança" "Protetor Facial" "Protetor Auricular Concha" "Luva de Raspa" "Avental de Raspa" "Botina de Segurança"
vincular "Torneamento" "Óculos de Segurança" "Protetor Auricular Concha" "Botina de Segurança"
vincular "Fresamento" "Óculos de Segurança" "Protetor Auricular Concha" "Botina de Segurança"
vincular "Furação" "Óculos de Segurança" "Protetor Auricular Concha" "Botina de Segurança"
vincular "Retificação" "Óculos de Segurança" "Protetor Facial" "Protetor Auricular Concha" "Luva de Raspa" "Botina de Segurança"
vincular "Fundição de metais" "Capacete de Segurança" "Óculos de Segurança" "Protetor Facial" "Luva Térmica" "Botina de Segurança" "Vestimenta Térmica" "Vestimenta Aluminizada"
vincular "Jateamento abrasivo" "Protetor Auricular Concha" "Luva de Raspa" "Botina de Segurança" "Respirador PFF2"
vincular "Pintura industrial" "Óculos de Segurança" "Luva Química" "Bota de Segurança" "Respirador com Filtro Químico"
vincular "Tratamento químico de metais" "Óculos de Segurança" "Protetor Facial" "Luva Química" "Bota de Segurança"
vincular "Montagem de estruturas" "Capacete de Segurança" "Óculos de Segurança" "Luva Anticorte" "Botina de Segurança"
vincular "Manutenção mecânica" "Capacete de Segurança" "Óculos de Segurança" "Protetor Facial" "Luva Anticorte" "Botina de Segurança"
vincular "Manutenção elétrica" "Capacete de Segurança" "Óculos de Segurança" "Protetor Facial" "Botina de Segurança"
vincular "Trabalho em altura" "Capacete de Segurança" "Óculos de Segurança" "Botina de Segurança" "Cinturão Paraquedista" "Talabarte de Segurança" "Trava-Quedas"
vincular "Limpeza industrial" "Óculos de Segurança" "Luva Química" "Botina de Segurança" "Respirador PFF2"
vincular "Carregamento e expedição" "Capacete de Segurança" "Óculos de Segurança" "Luva Anticorte" "Botina de Segurança"

echo "== Seed concluido =="
