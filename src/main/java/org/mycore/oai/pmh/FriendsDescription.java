package org.mycore.oai.pmh;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Implementation of the friends container description. 
 * 
 * @author Matthias Eichner
 */
public class FriendsDescription implements Description {

    final static Namespace NS_XSI = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	final static Namespace NS_FRIENDS = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/friends/");
    final static String SCHEMA_LOC_FRIENDS = "http://www.openarchives.org/OAI/2.0/friends/ http://www.openarchives.org/OAI/2.0/friends.xsd";

    private List<String> friendsList = null;

    public FriendsDescription(String... friends) {
    	this.friendsList = new ArrayList<String>();
    	for(String friend : friends) {
    		friendsList.add(friend);    		
    	}
    }

	@Override
	public Element toXML() {
		Element friends = new Element("friends", NS_FRIENDS);
		friends.setAttribute("schemaLocation", SCHEMA_LOC_FRIENDS, NS_XSI);
		friends.addNamespaceDeclaration(NS_XSI);
		for(String friendURL : friendsList) {
			friends.addContent(new Element("baseURL", NS_FRIENDS).setText(friendURL));
		}
		return friends;
	}

	public List<String> getFriendsList() {
		return friendsList;
	}

}
