import os, sys
import re

# raw version numbers
version_major    = 1
version_minor    = 1
version_build    = 1
version_revision = 0

# version name
version_name = "%d.%d.%d.%d" % (version_major, version_minor, version_build, version_revision)

# android version code
app_version_code = 3

def exit(code, msg):
    print msg
    os.system('pause') 
    sys.exit(code)

# update remote trackpad app
filename = "../remote/AndroidManifest.xml"
f = open(filename, "r")
if f:
    text = f.read();
    pattern = 'android:versionCode="\d+"'
    repl = 'android:versionCode="%d"' % app_version_code
    text = re.sub(pattern, repl, text)
    pattern = 'android:versionName=".+"'
    repl = 'android:versionName="%s"' % version_name
    text = re.sub(pattern, repl, text)
    f = open(filename, "w")
    f.write(text)
else:
    exit(1, "ERROR: '%s' not found" % filename)

# update remote trackpad server
filename = "../csserver/RemoteTrackpadServer/Properties/AssemblyInfo.cs"
f = open(filename, "r")
if f:
    text = f.read();
    pattern = 'AssemblyVersion\("\d+\.\d+\.\d+\.\d+"\)'
    repl = 'AssemblyVersion("%s")' % version_name
    text = re.sub(pattern, repl, text)
    pattern = 'AssemblyFileVersion\("\d+\.\d+\.\d+\.\d+"\)'
    repl = 'AssemblyFileVersion("%s")' % version_name
    text = re.sub(pattern, repl, text)
    f = open(filename, "w")
    f.write(text)
else:
    exit(1, "ERROR: '%s' not found" % filename)

# update remote trackpad server installer
filename = "../csserver/RemoteTrackpadServerInstaller/Product.wxs"
f = open(filename, "r")
if f:
    text = f.read();
    pattern = '<\?define VERSION="\d+\.\d+\.\d+\.\d+"\?>'
    repl = '<?define VERSION="%s"?>' % version_name
    text = re.sub(pattern, repl, text)
    f = open(filename, "w")
    f.write(text)
else:
    exit(1, "ERROR: '%s' not found" % filename)

