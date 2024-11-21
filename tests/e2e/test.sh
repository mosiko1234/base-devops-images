#!/bin/bash

set +e  # ביטול מצב 'exit on error'

echo "Starting E2E Tests for base image tools..."

# משתנה לספירת כשלים
fail_count=0

# פונקציה לבדיקת כלי וכל גרסה
check_tool() {
    local cmd=$1
    local version_flag=$2
    local description=$3

    echo -n "Testing $description ($cmd)... "
    if command -v "$cmd" &>/dev/null; then
        if ! eval "$cmd $version_flag" &>/dev/null; then
            echo "FAIL - $description version command failed"
            ((fail_count++))
        else
            echo "OK"
        fi
    else
        echo "FAIL - $description not found in PATH"
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
    echo "FAIL - Bash version not found"
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
    echo "FAIL - CA Certificates not found"
    ((fail_count++))
fi

# Test gnupg
check_tool "gpg" "--version" "GnuPG"

# Test apt-transport-https
echo -n "Testing apt-transport-https... "
if dpkg -l | grep -q "apt-transport-https"; then
    echo "OK"
else
    echo "FAIL - apt-transport-https not installed"
    ((fail_count++))
fi

# Test software-properties-common
echo -n "Testing software-properties-common... "
if command -v "add-apt-repository" &>/dev/null; then
    echo "OK"
else
    echo "FAIL - software-properties-common not installed"
    ((fail_count++))
fi

# Test AWS CLI
check_tool "aws" "--version" "AWS CLI"

# Test yq
check_tool "yq" "--version" "yq"

# Test Helm
check_tool "helm" "version --short" "Helm"


# Test OpenShift CLI
check_tool "oc" "version --client" "OpenShift CLI"

# Debugging OpenShift CLI if failed
if ! command -v "oc" &>/dev/null; then
    echo "DEBUG: OpenShift CLI not found in PATH"
elif ! oc version --client &>/dev/null; then
    echo "DEBUG: OpenShift CLI version command failed. Check glibc compatibility or dependencies."
fi

# Verify glibc version
echo -n "Testing glibc compatibility... "
if ldd --version | grep -q "2.34"; then
    echo "OK"
else
    echo "FAIL - glibc version is incompatible"
    fail_count=$((fail_count + 1))
fi


# Test ArgoCD CLI
check_tool "argocd" "version --client" "ArgoCD CLI"

# Test GitLab Runner CLI
check_tool "gitlab-runner" "--version" "GitLab Runner CLI"

if ! oc version --client &>/dev/null; then
    echo "DEBUG: OpenShift CLI Logs:"
    ls -l /usr/local/bin/oc || echo "DEBUG: File not found"
    ldd /usr/local/bin/oc || echo "DEBUG: Dependency check failed"
    echo "DEBUG: glibc version:"
    ldd --version || echo "glibc not found"
fi


# סיכום הבדיקות
echo ""
echo "Test Summary:"
if [ "$fail_count" -eq 0 ]; then
    echo "All tests passed successfully!"
else
    echo "Tests completed with $fail_count failures."
    exit 1
fi
