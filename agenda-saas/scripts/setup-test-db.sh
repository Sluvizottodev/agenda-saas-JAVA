# Script para criar banco de testes (agenda_saas_test) e popular dados de exemplo.
# Uso: ./scripts/setup-test-db.sh

set -euo pipefail

if [ -f .env.test ]; then
  export $(grep -v '^#' .env.test | xargs)
fi

DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-3306}
DB_USER=${DB_USER:-root}
DB_PASSWORD=${DB_PASSWORD:-}

SQL_FILE="init-database-test.sql"

echo "Criando banco de testes em ${DB_HOST}:${DB_PORT} com usuário ${DB_USER}"

mysql -h "${DB_HOST}" -P "${DB_PORT}" -u "${DB_USER}" $( [ -n "${DB_PASSWORD}" ] && echo -p"${DB_PASSWORD}" ) < "${SQL_FILE}"

echo "Concluído."
