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
