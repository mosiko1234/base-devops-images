ARG BASE_IMAGE
FROM ${BASE_IMAGE}
USER root
# Add Python installation
ARG PYTHON_VERSION=3.11
RUN apt-get update && apt-get install -y --no-install-recommends \
    python${PYTHON_VERSION} \
    python3-pip \
    python3-venv \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Upgrade pip and install common Python tools
RUN pip3 install --upgrade pip setuptools wheel --break-system-packages\
    && pip3 install \
        virtualenv \
        pytest \
        pylint \
        black \
        flake8

CMD ["python3"]
