package com.san.mbean.impl;

import java.lang.reflect.Constructor;
import java.util.Iterator;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;

public class DynamicBeanImpl extends NotificationBroadcasterSupport implements DynamicMBean {

	public DynamicBeanImpl() {
		buildDynamicMBeanInfo();
	}

	@Override
	public Object getAttribute(String attribute_name) throws AttributeNotFoundException, MBeanException, ReflectionException {

		// Check attribute_name to avoid NullPointerException later on
		if (attribute_name == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke a getter of " + dClassName + " with null attribute name");
		}

		// Call the corresponding getter for a recognized attribute_name
		if (attribute_name.equals("Test1")) {
			return getTest1();
		}
		if (attribute_name.equals("Test2")) {
			return getTest2();
		}

		// If attribute_name has not been recognized
		throw (new AttributeNotFoundException("Cannot find " + attribute_name + " attribute in " + dClassName));
	}

	@Override
	public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {

		// Check attribute to avoid NullPointerException later on
		if (attribute == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Cannot invoke a setter of " + dClassName + " with null attribute");
		}
		String name = attribute.getName();
		Object value = attribute.getValue();

		if (name.equals("Test1")) {
			// if null value, try and see if the setter returns any exception
			if (value == null) {
				try {
					setTest1(null);
				} catch (Exception e) {
					throw (new InvalidAttributeValueException("Cannot set attribute " + name + " to null"));
				}
			}
			// if non null value, make sure it is assignable to the attribute
			else if (String.class.isAssignableFrom(value.getClass())) {
				setTest1((String) value);
			} else {
				throw new InvalidAttributeValueException("Cannot set attribute " + name + " to a " + value.getClass().getName() + " object, String expected");
			}
		}
		// recognize an attempt to set a read-only attribute
		else if (name.equals("Test2")) {
			throw new AttributeNotFoundException("Cannot set attribute " + name + " because it is read-only");
		}

		// unrecognized attribute name
		else {
			throw new AttributeNotFoundException("Attribute " + name + " not found in " + this.getClass().getName());
		}
	}

	@Override
	public AttributeList getAttributes(String[] attributeNames) {

		// Check attributeNames to avoid NullPointerException later on
		if (attributeNames == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"), "Cannot invoke a getter of " + dClassName);
		}
		AttributeList resultList = new AttributeList();

		// if attributeNames is empty, return an empty result list
		if (attributeNames.length == 0)
			return resultList;

		// build the result attribute list
		for (int i = 0; i < attributeNames.length; i++) {
			try {
				Object value = getAttribute((String) attributeNames[i]);
				resultList.add(new Attribute(attributeNames[i], value));
			} catch (Exception e) {
				// print debug info but continue processing list
				e.printStackTrace();
			}
		}
		return (resultList);
	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {

		// Check attributes to avoid NullPointerException later on
		if (attributes == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList attributes cannot be null"), "Cannot invoke a setter of " + dClassName);
		}
		AttributeList resultList = new AttributeList();

		// if attributeNames is empty, nothing more to do
		if (attributes.isEmpty())
			return resultList;

		// try to set each attribute and add to result list if successful
		for (Iterator<?> i = attributes.iterator(); i.hasNext();) {
			Attribute attr = (Attribute) i.next();
			try {
				setAttribute(attr);
				String name = attr.getName();
				Object value = getAttribute(name);
				resultList.add(new Attribute(name, value));
			} catch (Exception e) {
				// print debug info but keep processing list
				e.printStackTrace();
			}
		}
		return (resultList);
	}

	@Override
	public Object invoke(String operationName, Object params[], String signature[]) throws MBeanException, ReflectionException {

		// Check operationName to avoid NullPointerException later on
		if (operationName == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"), "Cannot invoke a null operation in " + dClassName);
		}

		// Call the corresponding operation for a recognized name
		if (operationName.equals("reset")) {
			// this code is specific to the internal "reset" method:
			reset(); // no parameters to check
			return null; // and no return value
		} else {
			// unrecognized operation name:
			throw new ReflectionException(new NoSuchMethodException(operationName), "Cannot find the operation " + operationName + " in " + dClassName);
		}
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return dMBeanInfo;
	}

	private void buildDynamicMBeanInfo() {

		dAttributes[0] = new MBeanAttributeInfo("Test1", "java.lang.String", "Test1 attribute description.", true, true, false);
		dAttributes[1] = new MBeanAttributeInfo("Test2", "java.lang.Integer", "Number of times the Test1 string has been changed.", true, false, false);

		Constructor<?>[] constructors = this.getClass().getConstructors();
		dConstructors[0] = new MBeanConstructorInfo("Constructs a DynamicBeanImpl object", constructors[0]);

		MBeanParameterInfo[] params = null;
		dOperations[0] = new MBeanOperationInfo("reset", "reset Test1 and Test2 attributes to their initial values", params, "void", MBeanOperationInfo.ACTION);

		dNotifications[0] = new MBeanNotificationInfo(new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE }, AttributeChangeNotification.class.getName(), "This notification is emitted when the reset() method is called.");

		dMBeanInfo = new MBeanInfo(dClassName, dDescription, dAttributes, dConstructors, dOperations, dNotifications);

	}

	// PRIVATE VARIABLES

	private MBeanAttributeInfo[] dAttributes = new MBeanAttributeInfo[2];
	private MBeanConstructorInfo[] dConstructors = new MBeanConstructorInfo[1];
	private MBeanNotificationInfo[] dNotifications = new MBeanNotificationInfo[1];
	private MBeanOperationInfo[] dOperations = new MBeanOperationInfo[1];
	private MBeanInfo dMBeanInfo = null;
	private String dClassName = this.getClass().getName();
	private String dDescription = "Dynamic MBean description";

	// internal variables representing attributes
	private String test1 = "initial test1";
	private int test2 = 0;
	private int resetCounter = 0;

	// internal methods for getting attributes
	public String getTest1() {
		return test1;
	}

	// internal methods for getting attributes
	public int getTest2() {
		return test2;
	}

	// internal method for setting attribute
	public void setTest1(String s) {
		test1 = s;
		test2++;
	}

	// internal method for implementing the reset operation
	public void reset() {
		AttributeChangeNotification acn = new AttributeChangeNotification(this, 0, 0, "NbChanges reset", "NbChanges", "Integer", new Integer(resetCounter), new Integer(0));
		sendNotification(acn);
		test1 = "initial test1";
		test2 = 0;
		resetCounter++;
	}
}