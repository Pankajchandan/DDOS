# DDOS
Master Bot and Slave Bot for Distributed denial of service attack
The master bot is run on mater system and slave bots on any number of slave bots 

##Commands:
To run the Master bot use the following command line argument
-p portNumbber
To run SlaveBot Use the following Command line argument
-h hostaddressOf Master -p PortAddressOfMasterBot


The following commands are supported by master Bot
1. list
This command lists all the slave bots connected to master

2. connect slaveBotIp/all targetBotIP portNo Number of connection
This command directs the particular slaveBot or all slavebots to connect the target ip on the mentioned port number . number of connection states number of connection to the target.

3. disconnect (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) [TargetPort:all ifno port specified]
Close a number of connections to a given host Any violation of name or formatting specified in this document will result in zero grade. Your program must run via command line execution.

4. connect (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) TargetPortNumber [NumberOfConnections: 1 if not specified] [keepalive]
When the keepalive option is given, your client should select that socket option while creating the related connection.

5. connect (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) TargetPortNumber [NumberOfConnections: 1 if not specified] [url=path-to-be-provided-to webserver]
As an example, if you select to attack the Google search engine you will use:url=/#q=The slave will generate a HTTP request equivalent to: https://www.google.com/#q=YourRandomString

6. ipscan (IPAddressOrHostNameOfYourSlave|all) (IPAddressRage)When your slave process this command it must generate a list of IP addresses that replied to ICMP echo
messages and it should respond with empty list if none of the addresses responded to ICMP echo requests within 5 seconds. Your master must process the list of IP addresses that responded and present them as a comma separated list of IP addresses

7. tcpportscan (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) TargetPortNumberRage
Your slave should be able to scan what TCP ports area alived in the target host and create a comma separated list of those ports.

8. geoipscan (IPAddressOrHostNameOfYourSlave|all) (IPAddressRage)
When the slave process this command it  generates a list of IP addresses that replied to ICMP echo
messages along with their geolocation information. The command  responds with empty list if none of the addresses responded to ICMP echo requests within 5 seconds.

