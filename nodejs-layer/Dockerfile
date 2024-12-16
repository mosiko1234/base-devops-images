ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Accept Node.js version as build argument
ARG NODEJS_VERSION

# Set environment variables
ENV NODE_ENV=production \
    NPM_CONFIG_LOGLEVEL=error \
    DEBIAN_FRONTEND=noninteractive

# Switch to root for installations
USER root

# Install Node.js and npm
RUN set -ex \
    # Verify supported architecture
    && dpkgArch="$(dpkg --print-architecture)" \
    && case "${dpkgArch##*-}" in \
        amd64|arm64) echo "Architecture ${dpkgArch} is supported" ;; \
        *) echo "Architecture ${dpkgArch} is not supported" && exit 1 ;; \
    esac \
    # Install Node.js
    && apt-get update \
    && apt-get install -y curl \
    && curl -fsSL https://deb.nodesource.com/setup_${NODEJS_VERSION}.x | bash - \
    && apt-get install -y --no-install-recommends \
        nodejs \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    # Install yarn
    && npm install -g yarn \
    # Verify installations
    && echo "Node version: $(node --version)" \
    && echo "NPM version: $(npm --version)" \
    && echo "Yarn version: $(yarn --version)"

# Create app directory and set permissions
RUN mkdir -p /app \
    && chown -R devops:devops /app \
    && mkdir -p /home/devops/.npm \
    && chown -R devops:devops /home/devops/.npm

# Set working directory
WORKDIR /app

# Switch back to non-root user
USER devops

# Verify installation as non-root user
RUN node --version && npm --version && yarn --version

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 \
    CMD node --version || exit 1

CMD ["node"]