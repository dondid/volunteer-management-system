
import random
import uuid

# Base configuration
BASE_URL = "http://localhost:8080/volunteer-management-system/ws"
SERVICES = [
    {
        "name": "OrganizationService",
        "endpoint": "organizations",
        "entity": "Organization",
        "create_body": """<ws:addOrganization><arg0><name>Soap Org ${= (int)(Math.random()*10000) }</name><address>Test Address</address><email>org_${= (int)(Math.random()*10000) }@soap.com</email></arg0></ws:addOrganization>""",
        "update_body": """<ws:updateOrganization><arg0><id>${#Project#orgId}</id><name>Soap Org Updated</name><address>Updated Address</address><email>org_upd_${= (int)(Math.random()*10000) }@soap.com</email></arg0></ws:updateOrganization>""",
        "id_prop": "orgId",
        "deps": []
    },
    {
        "name": "VolunteerService",
        "endpoint": "volunteers",
        "entity": "Volunteer",
        "create_body": """<ws:addVolunteer><arg0><firstName>John</firstName><lastName>Soap</lastName><email>john.soap_${= (int)(Math.random()*10000) }@test.com</email></arg0></ws:addVolunteer>""",
        "update_body": """<ws:updateVolunteer><arg0><id>${#Project#volunteerId}</id><firstName>JohnUpdated</firstName><lastName>SoapUpdated</lastName><email>john.upd_${= (int)(Math.random()*10000) }@test.com</email></arg0></ws:updateVolunteer>""",
        "id_prop": "volunteerId",
        "deps": []
    },
    {
        "name": "ProjectService",
        "endpoint": "projects",
        "entity": "Project",
        "create_body": """<ws:addProject><arg0><title>Soap Project ${= (int)(Math.random()*10000) }</title><description>Test Desc</description><startDate>2026-06-01</startDate><organization><id>${#Project#orgId}</id></organization></arg0></ws:addProject>""",
        "update_body": """<ws:updateProject><arg0><id>${#Project#projectId}</id><title>Soap Project Updated</title><description>Updated Desc</description><startDate>2026-06-01</startDate><organization><id>${#Project#orgId}</id></organization></arg0></ws:updateProject>""",
        "id_prop": "projectId",
        "deps": ["orgId"]
    },
    {
        "name": "EventService",
        "endpoint": "events",
        "entity": "Event",
        "create_body": """<ws:addEvent><arg0><title>Soap Event ${= (int)(Math.random()*10000) }</title><location>Test Loc</location><eventDate>2026-07-01T10:00:00</eventDate><project><id>${#Project#projectId}</id></project></arg0></ws:addEvent>""",
        "update_body": """<ws:updateEvent><arg0><id>${#Project#eventId}</id><title>Soap Event Updated</title><location>Test Loc</location><eventDate>2026-07-01T10:00:00</eventDate><project><id>${#Project#projectId}</id></project></arg0></ws:updateEvent>""",
        "id_prop": "eventId",
        "deps": ["projectId"]
    },
    {
        "name": "AssignmentService",
        "endpoint": "assignments",
        "entity": "Assignment",
        "create_body": """<ws:addAssignment><arg0><role>Coordinator</role><assignmentDate>2026-06-02</assignmentDate><volunteer><id>${#Project#volunteerId}</id></volunteer><project><id>${#Project#projectId}</id></project></arg0></ws:addAssignment>""",
        "update_body": """<ws:updateAssignment><arg0><id>${#Project#assignmentId}</id><role>Manager</role><assignmentDate>2026-06-02</assignmentDate><volunteer><id>${#Project#volunteerId}</id></volunteer><project><id>${#Project#projectId}</id></project></arg0></ws:updateAssignment>""",
        "id_prop": "assignmentId",
        "deps": ["volunteerId", "projectId"]
    },
    {
        "name": "AttendanceService",
        "endpoint": "attendance",
        "entity": "Attendance",
        "create_body": """<ws:addAttendance><arg0><status>Present</status><hoursWorked>8</hoursWorked><assignment><id>${#Project#assignmentId}</id></assignment></arg0></ws:addAttendance>""",
        "update_body": """<ws:updateAttendance><arg0><id>${#Project#attendanceId}</id><status>Present</status><hoursWorked>10</hoursWorked><assignment><id>${#Project#assignmentId}</id></assignment></arg0></ws:updateAttendance>""",
        "id_prop": "attendanceId",
        "deps": ["assignmentId"]
    },
    {
        "name": "FeedbackService",
        "endpoint": "feedback",
        "entity": "Feedback",
        "create_body": """<ws:addFeedback><arg0><rating>5</rating><comment>Great work</comment><feedbackType>VOLUNTEER_TO_ORG</feedbackType><assignment><id>${#Project#assignmentId}</id></assignment></arg0></ws:addFeedback>""",
        "update_body": """<ws:updateFeedback><arg0><id>${#Project#feedbackId}</id><rating>4</rating><comment>Good work</comment><feedbackType>VOLUNTEER_TO_ORG</feedbackType><assignment><id>${#Project#assignmentId}</id></assignment></arg0></ws:updateFeedback>""",
        "id_prop": "feedbackId",
        "deps": ["assignmentId"]
    },
    {
        "name": "CertificateService",
        "endpoint": "certificates",
        "entity": "Certificate",
        "create_body": """<ws:addCertificate><arg0><certificateNumber>CERT-${= (int)(Math.random()*10000) }</certificateNumber><description>Excellence</description><totalHours>100</totalHours><volunteer><id>${#Project#volunteerId}</id></volunteer><project><id>${#Project#projectId}</id></project></arg0></ws:addCertificate>""",
        "update_body": """<ws:updateCertificate><arg0><id>${#Project#certId}</id><certificateNumber>CERT-${= (int)(Math.random()*10000) }</certificateNumber><description>Excellence Updated</description><totalHours>120</totalHours><volunteer><id>${#Project#volunteerId}</id></volunteer><project><id>${#Project#projectId}</id></project></arg0></ws:updateCertificate>""",
        "id_prop": "certId",
        "deps": ["volunteerId", "projectId"]
    },
    {
        "name": "SkillService",
        "endpoint": "skills",
        "entity": "Skill",
        "create_body": """<ws:addSkill><arg0><name>Soap Skill ${= (int)(Math.random()*10000) }</name><description>Skill Desc</description></arg0></ws:addSkill>""",
        "update_body": """<ws:updateSkill><arg0><id>${#Project#skillId}</id><name>Soap Skill Updated</name><description>Skill Desc Updated</description></arg0></ws:updateSkill>""",
        "id_prop": "skillId",
        "deps": []
    }
]

def generate_xml():
    project_id = str(uuid.uuid4())
    xml_parts = []
    
    # Header
    xml_parts.append(f'''<?xml version="1.0" encoding="UTF-8"?>
<con:soapui-project id="{project_id}" activeEnvironment="Default" name="VolunteerSystem-FULL" soapui-version="5.9.1" abortOnError="false" runType="SEQUENTIAL" resourceRoot="" xmlns:con="http://eviware.com/soapui/config">
  <con:settings/>''')

    # Interfaces
    for svc in SERVICES:
        wsdl_url = f"{BASE_URL}/{svc['endpoint']}?wsdl"
        if_id = str(uuid.uuid4())
        binding_name = f"{svc['name']}PortBinding"
        
        # We use a simplified interface definition that relies on SoapUI to fetch details on load/update
        # Note: We provide minimal structure so SoapUI recognizes it
        xml_parts.append(f'''
  <con:interface xsi:type="con:WsdlInterface" id="{if_id}" wsaVersion="NONE" name="{binding_name}" type="wsdl" bindingName="{{http://ws.soa.inf.ucv.ro/}}{binding_name}" soapVersion="1_1" anonymous="optional" definition="{wsdl_url}" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <con:settings/>
    <con:endpoints>
      <con:endpoint>{BASE_URL}/{svc['endpoint']}</con:endpoint>
    </con:endpoints>
  </con:interface>''')

    # TestSuites
    for svc in SERVICES:
        ts_id = str(uuid.uuid4())
        tc_id = str(uuid.uuid4())
        binding_name = f"{svc['name']}PortBinding"
        entity = svc['entity']
        
        xml_parts.append(f'''
  <con:testSuite id="{ts_id}" name="{entity}Tests">
    <con:settings/>
    <con:runType>SEQUENTIAL</con:runType>
    <con:testCase id="{tc_id}" failOnError="true" failTestCaseOnErrors="true" keepSession="false" maxResults="0" name="CRUD_Flow" searchProperties="true">
      <con:settings/>''')
        
        # Step 1: Create
        step1_id = str(uuid.uuid4())
        xml_parts.append(f'''
      <con:testStep type="request" name="Step1_Create" id="{step1_id}">
        <con:settings/>
        <con:config xsi:type="con:RequestStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:interface>{binding_name}</con:interface>
          <con:operation>add{entity}</con:operation>
          <con:request name="Step1_Create" id="{str(uuid.uuid4())}">
            <con:settings/>
            <con:encoding>UTF-8</con:encoding>
            <con:endpoint>{BASE_URL}/{svc['endpoint']}</con:endpoint>
            <con:request><![CDATA[<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.soa.inf.ucv.ro/">
   <soapenv:Header/>
   <soapenv:Body>
      {svc['create_body']}
   </soapenv:Body>
</soapenv:Envelope>]]></con:request>
            <con:assertion type="SOAP Response"/>
            <con:assertion type="SOAP Fault Assertion"/>
            <con:credentials><con:authType>No Authorization</con:authType></con:credentials>
          </con:request>
        </con:config>
      </con:testStep>''')

        # Step 2: Transfer
        trans_id = str(uuid.uuid4())
        xml_parts.append(f'''
      <con:testStep type="transfer" name="Transfer_ID" id="{trans_id}">
        <con:settings/>
        <con:config xsi:type="con:PropertyTransfersStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:transfers setNullOnMissingSource="true" transferTextContent="true" failOnError="true" ignoreEmpty="false" transferToAll="false" entitize="false" transferChildNodes="false">
            <con:name>{svc['id_prop']}</con:name>
            <con:sourceType>Response</con:sourceType>
            <con:sourceStep>Step1_Create</con:sourceStep>
            <con:sourcePath>//*:return/*:id</con:sourcePath>
            <con:targetType>{svc['id_prop']}</con:targetType>
            <con:targetStep>#Project#</con:targetStep>
          </con:transfers>
        </con:config>
      </con:testStep>''')

        # Step 3: Get
        step3_id = str(uuid.uuid4())
        xml_parts.append(f'''
      <con:testStep type="request" name="Step2_Get" id="{step3_id}">
        <con:settings/>
        <con:config xsi:type="con:RequestStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:interface>{binding_name}</con:interface>
          <con:operation>get{entity}ById</con:operation>
          <con:request name="Step2_Get" id="{str(uuid.uuid4())}">
            <con:settings/>
            <con:encoding>UTF-8</con:encoding>
            <con:endpoint>{BASE_URL}/{svc['endpoint']}</con:endpoint>
            <con:request><![CDATA[<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.soa.inf.ucv.ro/">
   <soapenv:Header/>
   <soapenv:Body>
      <ws:get{entity}ById>
         <arg0>${{#Project#{svc['id_prop']}}}</arg0>
      </ws:get{entity}ById>
   </soapenv:Body>
</soapenv:Envelope>]]></con:request>
            <con:assertion type="SOAP Response"/>
            <con:assertion type="SOAP Fault Assertion"/>
            <con:credentials><con:authType>No Authorization</con:authType></con:credentials>
          </con:request>
        </con:config>
      </con:testStep>''')
        
        # Step 4: Update
        step4_id = str(uuid.uuid4())
        xml_parts.append(f'''
      <con:testStep type="request" name="Step3_Update" id="{step4_id}">
        <con:settings/>
        <con:config xsi:type="con:RequestStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:interface>{binding_name}</con:interface>
          <con:operation>update{entity}</con:operation>
          <con:request name="Step3_Update" id="{str(uuid.uuid4())}">
            <con:settings/>
            <con:encoding>UTF-8</con:encoding>
            <con:endpoint>{BASE_URL}/{svc['endpoint']}</con:endpoint>
            <con:request><![CDATA[<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.soa.inf.ucv.ro/">
   <soapenv:Header/>
   <soapenv:Body>
      {svc['update_body']}
   </soapenv:Body>
</soapenv:Envelope>]]></con:request>
            <con:assertion type="SOAP Response"/>
            <con:assertion type="SOAP Fault Assertion"/>
            <con:credentials><con:authType>No Authorization</con:authType></con:credentials>
          </con:request>
        </con:config>
      </con:testStep>''')

        # Step 5: Delete
        step5_id = str(uuid.uuid4())
        xml_parts.append(f'''
      <con:testStep type="request" name="Step4_Delete" id="{step5_id}">
        <con:settings/>
        <con:config xsi:type="con:RequestStep" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <con:interface>{binding_name}</con:interface>
          <con:operation>delete{entity}</con:operation>
          <con:request name="Step4_Delete" id="{str(uuid.uuid4())}">
            <con:settings/>
            <con:encoding>UTF-8</con:encoding>
            <con:endpoint>{BASE_URL}/{svc['endpoint']}</con:endpoint>
            <con:request><![CDATA[<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.soa.inf.ucv.ro/">
   <soapenv:Header/>
   <soapenv:Body>
      <ws:delete{entity}>
         <arg0>${{#Project#{svc['id_prop']}}}</arg0>
      </ws:delete{entity}>
   </soapenv:Body>
</soapenv:Envelope>]]></con:request>
            <con:assertion type="SOAP Response"/>
            <con:assertion type="SOAP Fault Assertion"/>
            <con:credentials><con:authType>No Authorization</con:authType></con:credentials>
          </con:request>
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
  <con:wssContainer/>
  <con:oAuth2ProfileContainer/>
  <con:oAuth1ProfileContainer/>
  <con:sensitiveInformation/>
</con:soapui-project>''')

    with open("VolunteerSystem-FULL-soapui-project.xml", "w", encoding="utf-8") as f:
        f.write("\n".join(xml_parts))

if __name__ == "__main__":
    generate_xml()
