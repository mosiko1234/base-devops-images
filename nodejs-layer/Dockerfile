ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Accept Node.js version as build argument with default
ARG nodejs_VERSION

ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Accept Node.js version as build argument
ARG nodejs_VERSION

# Set environment variables
ENV NODE_ENV=production \
    NPM_CONFIG_LOGLEVEL=error \
    DEBIAN_FRONTEND=noninteractive

# Switch to root for installations
USER root

# Improved architecture detection for Node.js binary
RUN set -ex \
    && ARCH= && dpkgArch="$(dpkg --print-architecture)" \
    && case "${dpkgArch##*-}" in \
        amd64) ARCH='x64';; \
        arm64) ARCH='arm64';; \
        *) echo "unsupported architecture ${dpkgArch}"; exit 1;; \
    esac \
    # Install Node.js with verification
    && curl -fsSLO --compressed "https://nodejs.org/dist/v${nodejs_VERSION}/node-v${nodejs_VERSION}-linux-${ARCH}.tar.xz" \
    && curl -fsSLO --compressed "https://nodejs.org/dist/v${nodejs_VERSION}/SHASUMS256.txt" \
    && grep " node-v${nodejs_VERSION}-linux-${ARCH}.tar.xz\$" SHASUMS256.txt | sha256sum -c - \
    && mkdir -p /usr/local \
    && tar -xJf "node-v${nodejs_VERSION}-linux-${ARCH}.tar.xz" -C /usr/local --strip-components=1 --no-same-owner \
    && rm "node-v${nodejs_VERSION}-linux-${ARCH}.tar.xz" SHASUMS256.txt \
    && ln -s /usr/local/bin/node /usr/local/bin/nodejs \
    # Install latest NPM and Yarn
    && npm install -g npm@latest \
    && npm install -g yarn \
    # Verify installations
    && node --version \
    && npm --version \
    && yarn --version

# Set secure NPM configs
RUN npm config set unsafe-perm false \
    && npm config set audit true \
    && npm config set fund false

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