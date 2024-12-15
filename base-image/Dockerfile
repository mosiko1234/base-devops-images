# Start with a lightweight Linux base image
FROM ubuntu:20.04

# Set environment variables
ENV PATH="/root/.local/bin:$PATH"
ENV DEBIAN_FRONTEND=noninteractive
# Define shell with pipefail
SHELL ["/bin/bash", "-o", "pipefail", "-c"]


# Update system packages and install essential tools
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        software-properties-common \
    && add-apt-repository -y ppa:ubuntu-toolchain-r/test \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        build-essential \
        libxml2-utils \
        libc6 \
        libc6-dev \
        libc-bin \
        curl \
        git \
        jq \
        bash \
        unzip \
        wget \
        vim \
        ca-certificates \
        openssh-client \
        apt-transport-https \
        gnupg \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

    
# Install AWS CLI
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && ./aws/install \
    && rm -rf aws awscliv2.zip

# Install yq (YAML processor for DevOps pipelines)
RUN curl -sL "https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64" -o /usr/bin/yq \
    && chmod +x /usr/bin/yq

# Install Helm (Kubernetes package manager)
RUN curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

# Install OpenShift CLI (oc)
#RUN curl -L "https://mirror.openshift.com/pub/openshift-v4/clients/ocp/stable/openshift-client-linux.tar.gz" -o oc.tar.gz \
#    && tar -xvzf oc.tar.gz -C /usr/local/bin/ \
#    && chmod +x /usr/local/bin/oc \
#    && rm oc.tar.gz \
#    && /usr/local/bin/oc version --client || echo "OpenShift CLI installation verification failed"
# Install OpenShift CLI (oc) with a compatible version
RUN curl -L "https://mirror.openshift.com/pub/openshift-v4/clients/ocp/4.8.0/openshift-client-linux.tar.gz" -o oc.tar.gz \
    && tar -xvzf oc.tar.gz -C /usr/local/bin/ \
    && chmod +x /usr/local/bin/oc \
    && rm oc.tar.gz \
    && /usr/local/bin/oc version --client || echo "OpenShift CLI installation verification failed"

# Install ArgoCD CLI
RUN curl -sSL -o /usr/local/bin/argocd https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64 \
    && chmod +x /usr/local/bin/argocd

# Install GitLab Runner CLI
RUN curl -L "https://gitlab-runner-downloads.s3.amazonaws.com/v16.5.0/binaries/gitlab-runner-linux-amd64" -o /usr/local/bin/gitlab-runner \
    && chmod +x /usr/local/bin/gitlab-runner

# Set working directory
WORKDIR /workspace

# Default entrypoint
ENTRYPOINT ["/bin/bash"]