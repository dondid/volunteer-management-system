
import random
import uuid
import json

# Base configuration
BASE_URL = "http://localhost:8080/volunteer-management-system/api"
PROJECT_NAME = "VolunteerSystem-REST-FULL"

# Resource definitions with payloads
SERVICES = [
    {
        "name": "Organization",
        "resource": "organizations",
        "id_prop": "orgId",
        "create_json": {
            "name": "Rest Org ${= (int)(Math.random()*10000) }",
            "email": "rest_org_${= (int)(Math.random()*10000) }@test.com",
            "phone": "0711223344",
            "website": "https://resttest.com",
            "address": "Rest Lane",
            "registrationNumber": "REG-${= (int)(Math.random()*10000) }",
            "status": "ACTIVE"
        },
        "update_json": {
            "id": "${#Project#orgId}",
            "name": "Rest Org Updated",
            "email": "rest_org_upd_${= (int)(Math.random()*10000) }@test.com",
            "status": "ACTIVE"
        }
    },
    {
        "name": "Volunteer",
        "resource": "volunteers",
        "id_prop": "volunteerId",
        "create_json": {
            "firstName": "Rest",
            "lastName": "User ${= (int)(Math.random()*10000) }",
            "email": "rest_vol_${= (int)(Math.random()*10000) }@test.com",
            "phone": "0722123456",
            "dateOfBirth": "1990-01-01",
            "cnp": "1900101123456",
            "address": "Rest Street",
            "city": "Rest City",
            "county": "Rest County",
            "status": "ACTIVE"
        },
        "update_json": {
            "id": "${#Project#volunteerId}",
            "firstName": "RestUpdated",
            "lastName": "UserUpdated",
            "email": "rest_vol_upd_${= (int)(Math.random()*10000) }@test.com",
            "status": "ACTIVE"
        }
    },
    {
        "name": "Skill",
        "resource": "skills",
        "id_prop": "skillId",
        "create_json": {
            "name": "Rest Skill ${= (int)(Math.random()*10000) }",
            "category": "TECHNICAL",
            "description": "Rest Skill Desc"
        },
        "update_json": {
            "id": "${#Project#skillId}",
            "name": "Rest Skill Updated",
            "description": "Rest Skill Desc Updated"
        }
    },
    {
        "name": "Project",
        "resource": "projects",
        "id_prop": "projectId",
        "create_json": {
            "title": "Rest Project ${= (int)(Math.random()*10000) }",
            "description": "Rest Project Desc",
            "startDate": "2026-06-01",
            "endDate": "2026-12-31",
            "location": "Online",
            "status": "ACTIVE",
            "maxVolunteers": 50,
            "organization": { "id": "${#Project#orgId}" }
        },
        "update_json": {
            "id": "${#Project#projectId}",
            "title": "Rest Project Updated",
            "description": "Updated Desc",
            "organization": { "id": "${#Project#orgId}" }
        }
    },
    {
        "name": "Event",
        "resource": "events",
        "id_prop": "eventId",
        "create_json": {
            "title": "Rest Event ${= (int)(Math.random()*10000) }",
            "description": "Rest Event Desc",
            "eventDate": "2026-07-01T10:00:00",
            "location": "Rest Hall",
            "maxParticipants": 100,
            "status": "SCHEDULED",
            "project": { "id": "${#Project#projectId}" }
        },
        "update_json": {
            "id": "${#Project#eventId}",
            "title": "Rest Event Updated",
            "status": "COMPLETED",
            "project": { "id": "${#Project#projectId}" }
        }
    },
    {
        "name": "Assignment",
        "resource": "assignments",
        "id_prop": "assignmentId",
        "create_json": {
            "volunteer": { "id": "${#Project#volunteerId}" },
            "project": { "id": "${#Project#projectId}" },
            "role": "Rest Tester",
            "assignmentDate": "2026-06-02",
            "status": "ACCEPTED"
        },
        "update_json": {
            "id": "${#Project#assignmentId}",
            "role": "Rest Manager",
            "volunteer": { "id": "${#Project#volunteerId}" },
            "project": { "id": "${#Project#projectId}" }
        }
    },
    {
        "name": "Attendance",
        "resource": "attendance",
        "id_prop": "attendanceId",
        "create_json": {
            "assignment": { "id": "${#Project#assignmentId}" },
            "date": "2026-06-05",
            "hoursWorked": 5,
            "notes": "Rest checkin",
            "verifiedBy": "System"
        },
        "update_json": {
            "id": "${#Project#attendanceId}",
            "hoursWorked": 8,
            "assignment": { "id": "${#Project#assignmentId}" }
        }
    },
    {
        "name": "Feedback",
        "resource": "feedback",
        "id_prop": "feedbackId",
        "create_json": {
            "assignment": { "id": "${#Project#assignmentId}" },
            "rating": 5,
            "comment": "Rest Feedback",
            "feedbackType": "VOLUNTEER_TO_ORG",
            "feedbackDate": "2026-06-06T12:00:00"
        },
        "update_json": {
            "id": "${#Project#feedbackId}",
            "rating": 4,
            "comment": "Rest Feedback Updated",
            "assignment": { "id": "${#Project#assignmentId}" }
        }
    },
    {
        "name": "Certificate",
        "resource": "certificates",
        "id_prop": "certId",
        "create_json": {
            "volunteer": { "id": "${#Project#volunteerId}" },
            "project": { "id": "${#Project#projectId}" },
            "certificateNumber": "REST-CERT-${= (int)(Math.random()*10000) }",
            "description": "Rest Certificate",
            "issueDate": "2026-12-31",
            "totalHours": 100
        },
        "update_json": {
            "id": "${#Project#certId}",
            "description": "Rest Certificate Updated",
            "volunteer": { "id": "${#Project#volunteerId}" },
            "project": { "id": "${#Project#projectId}" }
        }
    }
]

def generate_xml():
    project_id = str(uuid.uuid4())
    xml_parts = []
    
    # Header
    xml_parts.append(f'''<?xml version="1.0" encoding="UTF-8"?>
<con:soapui-project id="{project_id}" activeEnvironment="Default" name="{PROJECT_NAME}" soapui-version="5.9.1" abortOnError="false" runType="SEQUENTIAL" resourceRoot="" xmlns:con="http://eviware.com/soapui/config">
  <con:settings/>''')

    # REST Interface
    interface_id = str(uuid.uuid4())
    xml_parts.append(f'''
  <con:interface xsi:type="con:RestService" id="{interface_id}" wadlVersion="http://wadl.dev.java.net/2009/02" name="RestService" type="rest" basePath="/volunteer-management-system/api" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <con:settings/>
    <con:definitionCache/>
    <con:endpoints>
      <con:endpoint>http://localhost:8080</con:endpoint>
    </con:endpoints>''')

    # Resources
    for svc in SERVICES:
        res_id = str(uuid.uuid4())
        # We need a main resource for POST (Create)
        # And a child resource for GET/PUT/DELETE by ID
        
        xml_parts.append(f'''
    <con:resource name="{svc['resource']}" path="{svc['resource']}" id="{res_id}">
      <con:settings/>
      <con:method name="Create" id="{str(uuid.uuid4())}" method="POST">
        <con:settings/>
        <con:parameters/>
        <con:representation type="REQUEST">
          <con:mediaType>application/json</con:mediaType>
          <con:params/>
        </con:representation>
        <con:representation type="RESPONSE">
          <con:mediaType>application/json</con:mediaType>
          <con:status>200 201</con:status>
          <con:params/>
        </con:representation>
        <con:request name="Request 1" id="{str(uuid.uuid4())}" mediaType="application/json">
          <con:settings/>
          <con:endpoint>http://localhost:8080</con:endpoint>
          <con:request/>
          <con:credentials><con:authType>No Authorization</con:authType></con:credentials>
          <con:parameters/>
        </con:request>
      </con:method>
      
      <con:resource name="ById" path="{{id}}" id="{str(uuid.uuid4())}">
        <con:settings/>
        <con:parameters>
           <con:parameter required="true"><con:name>id</con:name><con:value/><con:style>TEMPLATE</con:style><con:default/></con:parameter>
        </con:parameters>
        
        <con:method name="GetById" id="{str(uuid.uuid4())}" method="GET">
           <con:settings/>
           <con:parameters/>
           <con:request name="Request 1" id="{str(uuid.uuid4())}" mediaType="application/json">
             <con:settings/>
             <con:endpoint>http://localhost:8080</con:endpoint>
             <con:parameters/>
           </con:request>
        </con:method>
        
        <con:method name="Update" id="{str(uuid.uuid4())}" method="PUT">
           <con:settings/>
           <con:parameters/>
           <con:representation type="REQUEST">
             <con:mediaType>application/json</con:mediaType>
             <con:params/>
           </con:representation>
           <con:request name="Request 1" id="{str(uuid.uuid4())}" mediaType="application/json">
             <con:settings/>
             <con:endpoint>http://localhost:8080</con:endpoint>
             <con:parameters/>
           </con:request>
        </con:method>
        
        <con:method name="Delete" id="{str(uuid.uuid4())}" method="DELETE">
           <con:settings/>
           <con:parameters/>
           <con:request name="Request 1" id="{str(uuid.uuid4())}" mediaType="application/json">
             <con:settings/>
             <con:endpoint>http://localhost:8080</con:endpoint>
             <con:parameters/>
           </con:request>
        </con:method>
      </con:resource>
    </con:resource>''')
    xml_parts.append('  </con:interface>')

    # TestSuites
    for svc in SERVICES:
        ts_id = str(uuid.uuid4())
        tc_id = str(uuid.uuid4())
        
        xml_parts.append(f'''
  <con:testSuite id="{ts_id}" name="{svc['name']}RestTests">
    <con:settings/>
    <con:runType>SEQUENTIAL</con:runType>
    <con:testCase id="{tc_id}" failOnError="true" failTestCaseOnErrors="true" keepSession="false" maxResults="0" name="CRUD_Flow" searchProperties="true">
      <con:settings/>''')
        
        # Step 1: Create
        step1_id = str(uuid.uuid4())
        create_payload = json.dumps(svc['create_json']).replace('"', '&quot;')
        xml_parts.append(f'''
      <con:testStep type="restrequest" name="Step1_Create" id="{step1_id}">
        <con:settings/>
        <con:config service="RestService" resourcePath="/volunteer-management-system/api/{svc['resource']}" methodName="Create" xsi:type="con:RestRequestStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:restRequest name="Step1_Create" id="{str(uuid.uuid4())}" mediaType="application/json" postQueryString="false">
            <con:settings/>
            <con:endpoint>http://localhost:8080</con:endpoint>
            <con:request>{create_payload}</con:request>
            <con:assertion type="Valid HTTP Status Codes" id="{str(uuid.uuid4())}" name="Valid HTTP Status Codes"><con:configuration><codes>200,201</codes></con:configuration></con:assertion>
            <con:credentials><con:authType>No Authorization</con:authType></con:credentials>
            <con:parameters/>
          </con:restRequest>
        </con:config>
      </con:testStep>''')

        # Step 2: Transfer ID
        trans_id = str(uuid.uuid4())
        xml_parts.append(f'''
      <con:testStep type="transfer" name="Transfer_ID" id="{trans_id}">
        <con:settings/>
        <con:config xsi:type="con:PropertyTransfersStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:transfers setNullOnMissingSource="true" transferTextContent="true" failOnError="true" ignoreEmpty="false" transferToAll="false" entitize="false" transferChildNodes="false">
            <con:name>{svc['id_prop']}</con:name>
            <con:sourceType>Response</con:sourceType>
            <con:sourceStep>Step1_Create</con:sourceStep>
            <con:sourcePath>$.data.id</con:sourcePath>
            <con:targetType>{svc['id_prop']}</con:targetType>
            <con:targetStep>#Project#</con:targetStep>
            <con:type>JSONPATH</con:type>
          </con:transfers>
        </con:config>
      </con:testStep>''')

        # Step 3: Get
        step3_id = str(uuid.uuid4())
        xml_parts.append(f'''
      <con:testStep type="restrequest" name="Step2_Get" id="{step3_id}">
        <con:settings/>
        <con:config service="RestService" resourcePath="/volunteer-management-system/api/{svc['resource']}/{{id}}" methodName="GetById" xsi:type="con:RestRequestStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:restRequest name="Step2_Get" id="{str(uuid.uuid4())}" mediaType="application/json">
            <con:settings/>
            <con:endpoint>http://localhost:8080</con:endpoint>
            <con:request/>
            <con:assertion type="Valid HTTP Status Codes" id="{str(uuid.uuid4())}" name="Valid HTTP Status Codes"><con:configuration><codes>200</codes></con:configuration></con:assertion>
            <con:credentials><con:authType>No Authorization</con:authType></con:credentials>
            <con:parameters>
              <con:entry key="id" value="${{#Project#{svc['id_prop']}}}"/>
            </con:parameters>
          </con:restRequest>
        </con:config>
      </con:testStep>''')
        
        # Step 4: Update
        step4_id = str(uuid.uuid4())
        update_payload = json.dumps(svc['update_json']).replace('"', '&quot;')
        xml_parts.append(f'''
      <con:testStep type="restrequest" name="Step3_Update" id="{step4_id}">
        <con:settings/>
        <con:config service="RestService" resourcePath="/volunteer-management-system/api/{svc['resource']}/{{id}}" methodName="Update" xsi:type="con:RestRequestStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:restRequest name="Step3_Update" id="{str(uuid.uuid4())}" mediaType="application/json" postQueryString="false">
            <con:settings/>
            <con:endpoint>http://localhost:8080</con:endpoint>
            <con:request>{update_payload}</con:request>
            <con:assertion type="Valid HTTP Status Codes" id="{str(uuid.uuid4())}" name="Valid HTTP Status Codes"><con:configuration><codes>200</codes></con:configuration></con:assertion>
            <con:credentials><con:authType>No Authorization</con:authType></con:credentials>
             <con:parameters>
              <con:entry key="id" value="${{#Project#{svc['id_prop']}}}"/>
            </con:parameters>
          </con:restRequest>
        </con:config>
      </con:testStep>''')

        # Step 5: Delete
        step5_id = str(uuid.uuid4())
        xml_parts.append(f'''
      <con:testStep type="restrequest" name="Step4_Delete" id="{step5_id}">
        <con:settings/>
        <con:config service="RestService" resourcePath="/volunteer-management-system/api/{svc['resource']}/{{id}}" methodName="Delete" xsi:type="con:RestRequestStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:restRequest name="Step4_Delete" id="{str(uuid.uuid4())}" mediaType="application/json">
            <con:settings/>
            <con:endpoint>http://localhost:8080</con:endpoint>
            <con:request/>
            <con:assertion type="Valid HTTP Status Codes" id="{str(uuid.uuid4())}" name="Valid HTTP Status Codes"><con:configuration><codes>200,204</codes></con:configuration></con:assertion>
            <con:credentials><con:authType>No Authorization</con:authType></con:credentials>
            <con:parameters>
              <con:entry key="id" value="${{#Project#{svc['id_prop']}}}"/>
            </con:parameters>
          </con:restRequest>
        </con:config>
      </con:testStep>''')

        xml_parts.append('    </con:testCase>')
        xml_parts.append('  </con:testSuite>')

    # Footer
    xml_parts.append('''  <con:properties>
    <con:property><con:name>orgId</con:name><con:value/></con:property>
    <con:property><con:name>volunteerId</con:name><con:value/></con:property>
    <con:property><con:name>projectId</con:name><con:value/></con:property>
    <con:property><con:name>eventId</con:name><con:value/></con:property>
    <con:property><con:name>assignmentId</con:name><con:value/></con:property>
    <con:property><con:name>attendanceId</con:name><con:value/></con:property>
    <con:property><con:name>feedbackId</con:name><con:value/></con:property>
    <con:property><con:name>certId</con:name><con:value/></con:property>
    <con:property><con:name>skillId</con:name><con:value/></con:property>
  </con:properties>
</con:soapui-project>''')

    with open("VolunteerSystem-REST-FULL-soapui-project.xml", "w", encoding="utf-8") as f:
        f.write("\n".join(xml_parts))

if __name__ == "__main__":
    generate_xml()
