#!/bin/zsh

set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
JDK_DIR="$PROJECT_DIR/.jdk"
ARCHIVE="$JDK_DIR/OpenJDK21U-jdk_aarch64_mac_hotspot_21.0.8_9.tar.gz"
DOWNLOAD_URL="https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.8%2B9/OpenJDK21U-jdk_aarch64_mac_hotspot_21.0.8_9.tar.gz"

mkdir -p "$JDK_DIR"

if [[ ! -f "$ARCHIVE" ]]; then
  echo "Baixando JDK 21..."
  curl -L "$DOWNLOAD_URL" -o "$ARCHIVE"
fi

echo "Extraindo JDK..."
tar -xzf "$ARCHIVE" -C "$JDK_DIR"

echo
echo "JDK pronto em: $JDK_DIR"
echo "Agora rode: ./run.sh"
