#!/bin/bash

set -e

echo "Starting E2E Tests for base image tools..."

# Helper function to check a command and print its version
check_tool() {
    local cmd=$1
    local version_flag=$2
    local description=$3

    echo -n "Testing $description ($cmd)... "
    if command -v "$cmd" &>/dev/null; then
        "$cmd" "$version_flag" &>/dev/null || { echo "FAIL"; exit 1; }
        echo "OK"
    else
        echo "FAIL"
        exit 1
    fi
}

# Test curl
check_tool "curl" "--version" "cURL"

# Test git
check_tool "git" "--version" "Git"

# Test bash
echo -n "Testing Bash... "
if [ "$BASH_VERSION" ]; then
    echo "OK"
else
    echo "FAIL"
    exit 1
fi

# Test unzip
check_tool "unzip" "--help" "Unzip"

# Test wget
check_tool "wget" "--version" "Wget"

# Test vim
check_tool "vim" "--version" "Vim"

# Test ca-certificates
echo -n "Testing CA Certificates... "
if [ -f "/etc/ssl/certs/ca-certificates.crt" ]; then
    echo "OK"
else
    echo "FAIL"
    exit 1
fi

# Test gnupg
check_tool "gpg" "--version" "GnuPG"

# Test apt-transport-https
echo -n "Testing apt-transport-https... "
if apt-get -s update &>/dev/null; then
    echo "OK"
else
    echo "FAIL"
    exit 1
fi

# Test software-properties-common
echo -n "Testing software-properties-common... "
if command -v "add-apt-repository" &>/dev/null; then
    echo "OK"
else
    echo "FAIL"
    exit 1
fi

# Test AWS CLI
check_tool "aws" "--version" "AWS CLI"

# Test yq
check_tool "yq" "--version" "yq"

# Test Helm
check_tool "helm" "version --short" "Helm"

# Test OpenShift CLI
check_tool "oc" "version" "OpenShift CLI"

# Test ArgoCD CLI
check_tool "argocd" "version --client" "ArgoCD CLI"

# Test GitLab Runner CLI
check_tool "gitlab-runner" "--version" "GitLab Runner CLI"

echo "All tests passed successfully!"
