# Use your base image
ARG BASE_IMAGE
FROM ${BASE_IMAGE}

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive
ARG PYTHON_VERSION=3.11

USER root

# Add deadsnakes PPA and install Python
RUN echo "Using Python version: ${PYTHON_VERSION}" && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
    software-properties-common && \
    add-apt-repository -y ppa:deadsnakes/ppa && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
    python${PYTHON_VERSION} \
    python${PYTHON_VERSION}-venv \
    python${PYTHON_VERSION}-distutils \
    python3-pip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set Python version as default and configure pip3
RUN update-alternatives --remove python3 /usr/bin/python3 || true && \
    update-alternatives --install /usr/bin/python3 python3 /usr/bin/python${PYTHON_VERSION} 1 && \
    update-alternatives --set python3 /usr/bin/python${PYTHON_VERSION} && \
    update-alternatives --remove pip3 /usr/bin/pip3 || true && \
    ln -sf /usr/bin/pip3 /usr/local/bin/python${PYTHON_VERSION}-pip && \
    update-alternatives --install /usr/bin/pip3 pip3 /usr/bin/pip3 1 && \
    update-alternatives --set pip3 /usr/bin/pip3

# Verify installation
RUN python3 --version && pip3 --version

CMD ["python3"]
