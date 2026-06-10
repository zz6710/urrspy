package com.kayak.converter;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BaseBpmnXMLConverter;
import org.flowable.bpmn.converter.child.BaseChildElementParser;
import org.flowable.bpmn.converter.child.VariableListenerEventDefinitionParser;
import org.flowable.bpmn.converter.util.BpmnXMLUtil;
import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.alfresco.AlfrescoStartEvent;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;

/**
 * 支持StartEvent自定义属性
 */
public class StartEventXMLConverter extends BaseBpmnXMLConverter {
    protected static final List<ExtensionAttribute> defaultUserTaskAttributes = Arrays.asList(
            new ExtensionAttribute(ATTRIBUTE_FORM_FORMKEY));
    protected Map<String, BaseChildElementParser> childParserMap = new HashMap();

    public StartEventXMLConverter() {
        VariableListenerEventDefinitionParser variableListenerEventDefinitionParser = new VariableListenerEventDefinitionParser();
        this.childParserMap.put(variableListenerEventDefinitionParser.getElementName(), variableListenerEventDefinitionParser);
    }

    public Class<? extends BaseElement> getBpmnElementType() {
        return StartEvent.class;
    }

    protected String getXMLElementName() {
        return "startEvent";
    }

    protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
        String formKey = BpmnXMLUtil.getAttributeValue("formKey", xtr);
        StartEvent startEvent = null;
        if (StringUtils.isNotEmpty(formKey) && model.getStartEventFormTypes() != null && model.getStartEventFormTypes().contains(formKey)) {
            startEvent = new AlfrescoStartEvent();
        }

        if (startEvent == null) {
            startEvent = new StartEvent();
        }

        BpmnXMLUtil.addXMLLocation((BaseElement) startEvent, xtr);
        String elementId = xtr.getAttributeValue((String) null, "id");
        ((StartEvent) startEvent).setId(elementId);
        ((StartEvent) startEvent).setInitiator(BpmnXMLUtil.getAttributeValue("initiator", xtr));
        boolean interrupting = true;
        String interruptingAttribute = xtr.getAttributeValue((String) null, "isInterrupting");
        if ("false".equalsIgnoreCase(interruptingAttribute)) {
            interrupting = false;
        }

        ((StartEvent) startEvent).setInterrupting(interrupting);
        ((StartEvent) startEvent).setFormKey(formKey);
        String formValidation = BpmnXMLUtil.getAttributeValue("formFieldValidation", xtr);
        ((StartEvent) startEvent).setValidateFormFields(formValidation);
        String sameDeploymentAttribute = BpmnXMLUtil.getAttributeValue("sameDeployment", xtr);
        if ("false".equalsIgnoreCase(sameDeploymentAttribute)) {
            ((StartEvent) startEvent).setSameDeployment(false);
        }
        // FIXME: 允许自定义属性
        BpmnXMLUtil.addCustomAttributes(xtr, startEvent, defaultElementAttributes, defaultActivityAttributes, defaultUserTaskAttributes);
        this.parseChildElements(this.getXMLElementName(), (BaseElement) startEvent, this.childParserMap, model, xtr);
        return (BaseElement) startEvent;
    }

    protected void writeAdditionalAttributes(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {
        StartEvent startEvent = (StartEvent) element;
        this.writeQualifiedAttribute("initiator", startEvent.getInitiator(), xtw);
        this.writeQualifiedAttribute("formKey", startEvent.getFormKey(), xtw);
        this.writeQualifiedAttribute("formFieldValidation", startEvent.getValidateFormFields(), xtw);
        if (!startEvent.isSameDeployment()) {
            this.writeQualifiedAttribute("sameDeployment", "false", xtw);
        }

        if (startEvent.getEventDefinitions() != null && startEvent.getEventDefinitions().size() > 0) {
            this.writeDefaultAttribute("isInterrupting", String.valueOf(startEvent.isInterrupting()), xtw);
        }
        // FIXME: 允许自定义属性
        BpmnXMLUtil.writeCustomAttributes(startEvent.getAttributes().values(), xtw, defaultElementAttributes,
                defaultActivityAttributes, defaultUserTaskAttributes);
    }

    protected boolean writeExtensionChildElements(BaseElement element, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
        StartEvent startEvent = (StartEvent) element;
        didWriteExtensionStartElement = this.writeVariableListenerDefinition(startEvent, didWriteExtensionStartElement, xtw);
        didWriteExtensionStartElement = this.writeFormProperties(startEvent, didWriteExtensionStartElement, xtw);
        return didWriteExtensionStartElement;
    }

    protected void writeAdditionalChildElements(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {
        StartEvent startEvent = (StartEvent) element;
        this.writeEventDefinitions(startEvent, startEvent.getEventDefinitions(), model, xtw);
    }
}

