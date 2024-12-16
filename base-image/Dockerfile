# Start with Ubuntu 20.04
FROM ubuntu:20.04

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    PATH="/usr/local/bin:$PATH"

# Define shell with pipefail
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Create non-root user
RUN useradd -m -s /bin/bash devops \
    && mkdir -p /workspace \
    && chown devops:devops /workspace

# Update system packages and install essential tools in a single layer
# Group all installations to reduce layers and image size
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
        ca-certificates \
        openssh-client \
        apt-transport-https \
        gnupg \
    # Clean up apt cache and unnecessary files
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    && rm -rf /var/cache/apt/* \
    && rm -rf /tmp/* \
    && rm -rf /var/tmp/*

# Install tools in a single layer to reduce image size
RUN cd /tmp \
    # Install AWS CLI
    && curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && ./aws/install \
    && rm -rf aws awscliv2.zip \
    # Install yq
    && curl -sL "https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64" -o /usr/bin/yq \
    && chmod +x /usr/bin/yq \
    # Install Helm
    && curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash \
    # Install OpenShift CLI
    && curl -L "https://mirror.openshift.com/pub/openshift-v4/clients/ocp/4.8.0/openshift-client-linux.tar.gz" -o oc.tar.gz \
    && tar -xvzf oc.tar.gz -C /usr/local/bin/ \
    && chmod +x /usr/local/bin/oc \
    && rm -f oc.tar.gz \
    && rm -f /usr/local/bin/README.md \
    # Install ArgoCD CLI
    && curl -sSL -o /usr/local/bin/argocd https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64 \
    && chmod +x /usr/local/bin/argocd \
    # Install GitLab Runner
    && curl -L "https://gitlab-runner-downloads.s3.amazonaws.com/v16.5.0/binaries/gitlab-runner-linux-amd64" -o /usr/local/bin/gitlab-runner \
    && chmod +x /usr/local/bin/gitlab-runner \
    # Clean up
    && rm -rf /tmp/*

# Set proper permissions for all binaries
RUN chmod 755 /usr/local/bin/* \
    && chmod 755 /usr/bin/yq

# Set working directory and permissions
WORKDIR /workspace
RUN chown devops:devops /workspace

# Switch to non-root user
USER devops

# Default entrypoint
ENTRYPOINT ["/bin/bash"]