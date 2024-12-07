# Use base image
ARG BASE_IMAGE
FROM ${BASE_IMAGE}

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# Accept Python version as an argument
ARG PYTHON_VERSION

# Install core dependencies for Python
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        build-essential \
        libssl-dev \
        zlib1g-dev \
        libbz2-dev \
        libreadline-dev \
        libsqlite3-dev \
        libffi-dev \
        liblzma-dev \
        software-properties-common \
        wget && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*


# Add deadsnakes PPA for Python installation
RUN add-apt-repository ppa:deadsnakes/ppa && apt-get update && \
    apt-get install -y --no-install-recommends \
        python${PYTHON_VERSION} python${PYTHON_VERSION}-dev python${PYTHON_VERSION}-distutils && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install pip using get-pip.py
RUN wget https://bootstrap.pypa.io/get-pip.py && \
    python${PYTHON_VERSION} get-pip.py && \
    rm get-pip.py

# Configure Python alternatives
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python${PYTHON_VERSION} 1 && \
    update-alternatives --set python3 /usr/bin/python${PYTHON_VERSION}

# Install runtime dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
        python3-apt && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Skip ldd if running under ARM emulation
RUN [ "$(uname -m)" != "aarch64" ] || echo "Skipping ldd on ARM"

# Verify Python and pip versions
RUN python3 --version && pip3 --version

# Set default entrypoint and command
ENTRYPOINT ["/bin/bash", "-c"]
CMD ["python3 --version"]
