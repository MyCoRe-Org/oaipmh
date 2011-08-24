package org.mycore.oai.pmh;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

/**
 * Implementation of the friends container description. 
 * 
 * @author Matthias Eichner
 */
public class FriendsDescription implements Description {

    private List<String> friendsList = null;

    public FriendsDescription() {
        this.friendsList = new ArrayList<String>();
    }

    public FriendsDescription(String... friends) {
    	this.friendsList = new ArrayList<String>();
    	for(String friend : friends) {
    		friendsList.add(friend);    		
    	}
    }

	@Override
	public Element toXML() {
		Element friends = new Element("friends", OAIConstants.NS_OAI_FRIENDS);
		friends.setAttribute("schemaLocation", OAIConstants.SCHEMA_LOC_OAI_FRIENDS, OAIConstants.NS_XSI);
		friends.addNamespaceDeclaration(OAIConstants.NS_XSI);
		for(String friendURL : friendsList) {
			friends.addContent(new Element("baseURL", OAIConstants.NS_OAI_FRIENDS).setText(friendURL));
		}
		return friends;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void fromXML(Element friends) {
        List<Element> friendList = friends.getChildren("baseURL", OAIConstants.NS_OAI_FRIENDS);
        for(Element friend : friendList) {
            this.friendsList.add(friend.getText());
        }
	}

	public List<String> getFriendsList() {
		return friendsList;
	}

}
