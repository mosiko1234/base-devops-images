# Builder stage
FROM ubuntu:20.04 AS builder

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    LD_LIBRARY_PATH="/opt/glibc-2.34/lib:$LD_LIBRARY_PATH"

# Install build tools and dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    libc6 \
    libc6-dev \
    libc-bin \
    curl \
    git \
    bash \
    unzip \
    wget \
    vim \
    ca-certificates \
    gnupg \
    apt-transport-https \
    software-properties-common \
    gawk \
    bison \
    python3 \
    python3-dev \
    python3-pip \
    gettext \
    autoconf \
    automake \
    libtool \
    texinfo \
    zlib1g-dev \
    libssl-dev \
    libffi-dev \
    libreadline-dev \
    libbz2-dev \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install GLIBC
RUN wget http://ftp.gnu.org/gnu/libc/glibc-2.34.tar.gz && \
    tar -xvzf glibc-2.34.tar.gz && \
    cd glibc-2.34 && \
    mkdir build && cd build && \
    ../configure --prefix=/opt/glibc-2.34 --disable-werror --enable-debug && \
    make -j$(nproc) || { echo "Make failed"; exit 1; } && \
    make install DESTDIR=/tmp/glibc || { echo "Make install failed"; exit 1; } && \
    cp -r /tmp/glibc/opt/glibc-2.34/* /opt/glibc-2.34/ && \
    cd ../.. && rm -rf glibc-2.34 glibc-2.34.tar.gz

# Verify GLIBC installation
RUN /opt/glibc-2.34/lib/ld-2.34.so --version

# Final stage
FROM ubuntu:20.04

# Copy GLIBC from builder
COPY --from=builder /opt/glibc-2.34 /opt/glibc-2.34

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    LD_LIBRARY_PATH="/opt/glibc-2.34/lib:$LD_LIBRARY_PATH"

# Install runtime dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    bash \
    ca-certificates \
    curl \
    git \
    vim \
    && apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /workspace

# Define default entrypoint
ENTRYPOINT ["/bin/bash"]
