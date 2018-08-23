FROM ubuntu:16.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get -qq update && apt-get dist-upgrade -y  && apt-get install -qq --no-install-recommends --allow-unauthenticated -y \
  dbus \
  openssh-client \
  x11vnc \
  xvfb \
  bash \
  xterm \
  fluxbox \
  python-pip \
  git \
  nano \
  python-setuptools \
  wget \
  xvfb \
  xorg \
  && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN pip install --no-cache-dir \
  robotframework

RUN mkdir ~/.vnc
RUN x11vnc -storepasswd 1234 ~/.vnc/passwd
COPY entrypoint.sh /entrypoint.sh
EXPOSE 5900

ENTRYPOINT ["x11vnc", "-create", "-forever", "-env", "FD_PROG=/entrypoint.sh", "-env", "X11VNC_CREATE_GEOM=${1:-1024x768x16}", "-usepw"]
