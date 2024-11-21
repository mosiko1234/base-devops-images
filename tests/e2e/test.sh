#!/bin/bash

set +e  # ביטול מצב 'exit on error'

echo "Starting E2E Tests for base image tools..."

# משתנה לספירת כשלים
fail_count=0

# Helper function to check a command and print its version
check_tool() {
    local cmd=$1
    local version_flag=$2
    local description=$3

    echo -n "Testing $description ($cmd)... "
    if command -v "$cmd" &>/dev/null; then
        if ! "$cmd" "$version_flag" &>/dev/null; then
            echo "FAIL"
            ((fail_count++))
        else
            echo "OK"
        fi
    else
        echo "FAIL"
        ((fail_count++))
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
    ((fail_count++))
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
    ((fail_count++))
fi

# Test gnupg
check_tool "gpg" "--version" "GnuPG"

# Test apt-transport-https
echo -n "Testing apt-transport-https... "
if apt-get -s update &>/dev/null; then
    echo "OK"
else
    echo "FAIL"
    ((fail_count++))
fi

# Test software-properties-common
echo -n "Testing software-properties-common... "
if command -v "add-apt-repository" &>/dev/null; then
    echo "OK"
else
    echo "FAIL"
    ((fail_count++))
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

# סיכום הבדיקות
if [ "$fail_count" -eq 0 ]; then
    echo "All tests passed successfully!"
else
    echo "Tests completed with $fail_count failures."
    exit 1
fi