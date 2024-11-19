# Use your base image
ARG BASE_IMAGE
FROM ${BASE_IMAGE}

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive
ARG PYTHON_VERSION

# Install Python and pip
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

# Set default python and pip versions
RUN update-alternatives --remove python3 /usr/bin/python3 || true && \
    update-alternatives --install /usr/bin/python3 python3 /usr/bin/python${PYTHON_VERSION} 1 && \
    update-alternatives --set python3 /usr/bin/python${PYTHON_VERSION} && \
    ln -sf /usr/bin/pip3 /usr/local/bin/python${PYTHON_VERSION}-pip && \
    if [ ! -e /usr/bin/pip3 ]; then \
        ln -sf /usr/bin/pip3 /usr/local/bin/pip3; \
    fi

# Upgrade pip to the latest version and install setuptools and wheel
RUN python3 -m pip install --upgrade pip setuptools wheel

# Verify installation
RUN python3 --version && pip3 --version

CMD ["python3"]
