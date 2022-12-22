git config --global user.name <username>
git config --global user.email <mailaddress>
ssh-keygen -t ed25519 -C "your_email@example.com"


git config --global user.name samzhu
git config --global user.email spike19820318@gmail.com

ssh-keygen -t ed25519 -f github_key -P "" -C "spike19820318@gmail.com"
cat github_key.pub
ssh-add ./ed25519
ssh-add ~/.ssh/github_key

ssh-keygen -t ed25519 -P "" -C "spike19820318@gmail.com"
ssh-add ~/.ssh/github_key
cat /root/.ssh/id_ed25519.pub



``` bash
# ssh-keygen -t rsa -b 1024 -P "" -C "spike19820318@gmail.com"
Generating public/private rsa key pair.
Enter file in which to save the key (/root/.ssh/id_rsa): 
Your identification has been saved in /root/.ssh/id_rsa
Your public key has been saved in /root/.ssh/id_rsa.pub
The key fingerprint is:
SHA256:ekR5KBrE/tDZzPSVS4oWPDm1Rb3cDvuuy+2zi0oyWYw spike19820318@gmail.com
The key's randomart image is:
+---[RSA 4096]----+
|   ..  . o..oo   |
|   ..   Bo o+ .  |
|   ....*+*o+ o o |
|    oooo*.= . + .|
|    .o .SE o   + |
|      .o  o   . .|
|      . .+ .   . |
|       .  +  ..o.|
|           ...+BB|
+----[SHA256]-----+
```

cat /root/.ssh/id_rsa.pub


git clone git@github.com:samzhu/springboot-cloud-run-lab-2022-0221.git

https://hackmd.io/@CynthiaChuang/Generating-a-Ssh-Key-and-Adding-It-to-the-Github