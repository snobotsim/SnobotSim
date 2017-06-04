

import re

with open(r"/home/pj/GitHub/FIRST/2017/CppSimulator/ctre_override/ORIGINAL.cpp") as f:
	contents = f.read()

matches = re.findall(r" +(.*?) (.*)\((.*)\)(.*);", contents, re.MULTILINE)


output = ""
output += '#include "CANTalon.h"\n'
output += '#include <iostream>\n'
output += '#define LOG_UNSUPPORTED() std::cerr << "Unsupported function at " << __FILE__ << ":" << __LINE__ << std::endl\n'

for match in matches:
	ret, func, args, trailing = match

	trailing = trailing.replace("override", "")
	ret = ret.replace("virtual", "")

	output += ret + " CANTalon::" + func + "(" + args + ") " + trailing + "\n{\n    LOG_UNSUPPORTED();\n"
	if ret == "int" or ret == "long" or ret == "double":
		output += "    return 0;\n"
	elif ret == "bool":
		output += "    return false;\n"
	elif ret == "std::string":
		output += '    return "";\n'

	output += "}\n\n"

with open(r"/home/pj/GitHub/FIRST/2017/CppSimulator/ctre_override/cpp/lib/CANTalon.cpp", 'w') as f:
	f.write(output)

