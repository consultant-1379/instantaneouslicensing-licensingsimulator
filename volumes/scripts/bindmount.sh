#!/bin/bash
# File mounted as: /etc/sftp.d/bindmount.sh


function bindmount() {
    if [ -d "$1" ]; then
        mkdir -p "$2"
    fi
    mount --bind $3 "$1" "$2"
}

# Remember permissions, you may have to fix them:
chown -R enmuser: /data/Store/LicenseKeys
bindmount /data/Store/LicenseKeys /home/enmuser/data/Store/LicenseKeys