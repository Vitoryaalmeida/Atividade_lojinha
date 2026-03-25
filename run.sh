#!/bin/zsh

set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
OUT_DIR="$PROJECT_DIR/out"
LOCAL_JAVA_HOME="$(find "$PROJECT_DIR/.jdk" -maxdepth 3 -type d -path '*/Contents/Home' | head -n 1)"

if [[ -n "${LOCAL_JAVA_HOME:-}" && -d "$LOCAL_JAVA_HOME" ]]; then
  export JAVA_HOME="$LOCAL_JAVA_HOME"
  export PATH="$JAVA_HOME/bin:$PATH"
fi

if ! command -v javac >/dev/null 2>&1; then
  echo "JDK não encontrado."
  echo "Se quiser, execute primeiro: ./setup-java.sh"
  exit 1
fi

mkdir -p "$OUT_DIR"
javac -d "$OUT_DIR" $(find "$PROJECT_DIR/src" -name "*.java")
java -cp "$OUT_DIR" br.com.lojinha.Main "$@"
