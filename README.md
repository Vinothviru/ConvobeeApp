# ConvobeeApp
First production application for building communication - Powered by OnePercent


Steps to do after deploying build:

1. Need to call "/addslots" POST API by setting number of days to add slots for meeting in body of the request, so that the slots will be created at the backend but this API is accessbile only for ROLE_ADMIN role users. So need to signup via postman and need to provide role as ROLE_ADMIN before accessing this API.
{
	"numberOfDays" : "10"
}

2. Need to call '/addinterests' POST API by setting the below in the body. so that the slots will be created at the backend but this API is accessbile only for ROLE_ADMIN role users.
{
"interests":["blogging", "cooking", "dance", "gaming", "music", "pet care", "reading books", "sports", "technology", "traveling", "speak"]
}

3. Use this document for lombok installation - https://stackoverflow.com/questions/65017414/mac-os-lombok-installation-in-sts

4. Use this video to setup Mysql in laptop - https://www.youtube.com/watch?v=-BDbOOY9jsc&ab_channel=AmitThinks

Documents related to Convobee:

Initial doc -> https://docs.google.com/document/d/17tS-evNOHsRp_qtwBYlrsNWEJ-XGXW6bmMjiVbGdUI4

API Contract -> https://docs.google.com/spreadsheets/d/1wfSSGtuGSoqc8Z91-sOMoBX8ycVG7oz278BEFOSEc-g/edit#gid=0

UX -> https://www.figma.com/proto/Cz0CzjgTTyK38zCqxRRkUZ/Untitled?page-id=0%3A1&node-id=156%3A236&viewport=241%2C48%2C0.31&scaling=min-zoom&starting-point-node-id=156%3A236

Database -> https://online.visual-paradigm.com/community/share/convobee-erd-omufovdgl 
