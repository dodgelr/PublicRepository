package lyon.future.LYON_future4;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class Businesscard {

	Participant participant;
	ArrayList<Businesscard.Entity> entity;


	public class Participant {
		String scheme;
		String value;
	}

	public class Entity {
		String countryCode;
		Name name;
		String regdate;
	}

	public class Name {
		String name;
		String language;
	}

	public class nameEntity{
		/* [array] return Name, enterprise number and countrycode */
		String name;
		String entNum;
		String countryCode;

	}

	public static void declare() {

		Businesscard bc = new Businesscard();	
		Businesscard.Participant p = bc.new Participant();
		p.scheme = "sample";
		p.value = "the value";
		bc.participant = p;
		System.out.println("EYY "  + bc.participant.scheme);

	}

	public static void Display(Businesscard bc) {
		System.out.println("scheme: " + bc.participant.scheme);
		System.out.println("value: " + bc.participant.value);
		int ctr = 0;
		for (Businesscard.Entity ent:bc.entity) {
			ctr++;
			System.out.println("	Entity Countrycode: " + ent.countryCode);
			System.out.println("	Entity Regdate: " + ent.regdate);
			System.out.println("	Entity: Name: Language " + ent.name.language);
			System.out.println("	Entity: Name: name: " + ent.name.name);
		}	
		System.out.println("list length: " + ctr);		

	}

	public static void DisplayCards(ArrayList<Businesscard> bc) {
		for (Businesscard b:bc) {
			Display(b);
		}
	}

	public static NodeList XMLreader() {
		System.out.println("Reading XML...");

		try {
			//File file = new File("E:\\DODGE\\CopyAndPaste\\directory-export-business-cards-no-doc-types.xml");
			File file = new File("C://Users//dodge lester riveral//eclipse-workspace//LYON-future4//src//main//resources//directory-export-business-cards-no-doc-types.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

			NodeList bcards = doc.getElementsByTagName("businesscard");
			return bcards;

		} catch (Exception e) {
			System.out.println("theres an error! " + e);
			return null;
		}

	}

	public static Businesscard GetById(String ICD, String entID) {
		/* single return businesscard */
		NodeList bcards = XMLreader();

		Businesscard bb =  new Businesscard();


		ArrayList<Businesscard.Entity> fList = new ArrayList<Businesscard.Entity>();

		Boolean stopLoop = false;
		Boolean getEntity = false;

		for (int i = 0; i < bcards.getLength(); i++) {
			if (!stopLoop) {
				Node bc = bcards.item(i);
				if (bc.getNodeType() == Node.ELEMENT_NODE) {
					Element card = (Element) bc;
					NodeList pAndE = card.getChildNodes();
					for (int j = 0; j < pAndE.getLength(); j++) {
						Node pE = pAndE.item(j);
						if (pE.getNodeType() == Node.ELEMENT_NODE) {
							Element pr = (Element) pE;
							if (pr.getTagName().contentEquals("participant")) {
								String v = pr.getAttribute("value");
								String[] vSplit = v.split(":");
								if (vSplit.length == 2) {
									if (vSplit[0].contentEquals(ICD) && vSplit[1].contentEquals(entID)) {
										Participant prt = bb.new Participant();
										prt.scheme = pr.getAttribute("scheme");
										prt.value = v;
										bb.participant = prt;
										stopLoop = true;
										getEntity = true;
									}
								}
							}
							if (pr.getTagName().contentEquals("entity")) {
								if (getEntity) {
									Businesscard.Entity ent  = bb.new Entity();
									ent.countryCode = pr.getAttribute("countrycode");
									NodeList entKid = pr.getChildNodes();
									for (int k = 0; k < entKid.getLength(); k++) {
										Node nm = entKid.item(k);
										if (nm.getNodeType() == Node.ELEMENT_NODE) {
											Element nmRg = (Element) nm;
											if (nmRg.getTagName().contentEquals("regdate")) {
												ent.regdate = nmRg.getTextContent();
											}
											if (nmRg.getTagName().contentEquals("name")) {
												Businesscard.Name nn =  bb.new Name();
												nn.name = nmRg.getAttribute("name");
												nn.language = nmRg.getAttribute("language");
												ent.name = nn;
											}
										}
									}

									fList.add(ent);
								}
							}

						}

					}
				}
			}
		}
		bb.entity = fList;
		return bb;
	}

	public static ArrayList<Businesscard.nameEntity> SearchByName(String name) {
		/* [array] return Name, enterprise number and countrycode */
		ArrayList<Businesscard.nameEntity> bCrds = new ArrayList<Businesscard.nameEntity>();
		NodeList bcards = XMLreader();
		Boolean getBcard;
		for (int i = 0; i < bcards.getLength(); i++) {
			getBcard = false;
			Node bc = bcards.item(i);
			if (bc.getNodeType() == Node.ELEMENT_NODE) {
				Element card = (Element) bc;
				NodeList pAndE = card.getChildNodes();
				String currentID = "";
				String pSc = "";
				String previousName = "";
				String currentName = "";
				for (int j = 0; j < pAndE.getLength(); j++) {
					Node pE = pAndE.item(j);
					if (pE.getNodeType() == Node.ELEMENT_NODE) {
						Element pr = (Element) pE;
						if (pr.getTagName().contentEquals("participant")) {
							currentID = pr.getAttribute("value");
							pSc = pr.getAttribute("scheme");

						}
						String eCntry = "";
						if (pr.getTagName().contentEquals("entity")) {
							eCntry = pr.getAttribute("countrycode");
							String regD;
							NodeList entKid = pr.getChildNodes();
							for (int k = 0; k < entKid.getLength(); k++) {
								Node nm = entKid.item(k);
								if (nm.getNodeType() == Node.ELEMENT_NODE) {
									Element nmRg = (Element) nm;
									if (nmRg.getTagName().contentEquals("name")) {
										if (nmRg.getAttribute("name").contains(name)) {
											currentName = nmRg.getAttribute("name");
											if (!currentName.contentEquals(previousName)) {
												Businesscard b =  new Businesscard();
												Businesscard.nameEntity ent = b.new nameEntity();
												ent.entNum = currentID;
												ent.countryCode = eCntry;
												ent.name = currentName;
												bCrds.add(ent);
											}
										}
									}
								}

							}
							previousName = currentName;
						}
					}

				}

			}

		}
		return bCrds;
	}

	public static Businesscard getByName(String name) {
		NodeList bcards = XMLreader();
		Businesscard b =  new Businesscard();
		ArrayList<Businesscard.Entity> eList  = new ArrayList<Businesscard.Entity>();
		for (int i = 0; i < bcards.getLength(); i++) {
			Node bc = bcards.item(i);
			if (bc.getNodeType() == Node.ELEMENT_NODE) {
				Element card = (Element) bc;
				NodeList pAndE = card.getChildNodes();
				String currentID = "";
				String pSc = "";
				for (int j = 0; j < pAndE.getLength(); j++) {
					Node pE = pAndE.item(j);
					if (pE.getNodeType() == Node.ELEMENT_NODE) {
						Element pr = (Element) pE;
						if (pr.getTagName().contentEquals("participant")) {
							currentID = pr.getAttribute("value");
							pSc = pr.getAttribute("scheme");

						}
						String eCntry = "";
						if (pr.getTagName().contentEquals("entity")) {
							Businesscard.Participant prt =  b.new Participant();
							Businesscard.Entity ent = b.new Entity();
							Businesscard.Name nme =  b.new Name();
							eCntry = pr.getAttribute("countrycode");
							String regD;
							Boolean getRegD = false;
							NodeList entKid = pr.getChildNodes();
							for (int k = 0; k < entKid.getLength(); k++) {
								Node nm = entKid.item(k);
								if (nm.getNodeType() == Node.ELEMENT_NODE) {
									Element nmRg = (Element) nm;
									if (nmRg.getTagName().contentEquals("name")) {
										if (nmRg.getAttribute("name").contentEquals(name)) {
											prt.scheme = pSc;
											prt.value = currentID;
											ent.countryCode = eCntry;
											nme.name = nmRg.getAttribute("name");
											System.out.println(nme.name);
											nme.language = nmRg.getAttribute("language");
											System.out.println(nme.language);
											b.participant = prt; 
											getRegD = true;											
										}
									}
									if (nmRg.getTagName().contentEquals("regdate")) {
										if  (getRegD) {	
											regD = nmRg.getTextContent();
											ent.regdate = regD;
											ent.name = nme;
										}

									}
								}
							}
							if (getRegD) {
								eList.add(ent);
							}
						}
					}

				}
				b.entity = eList;
			}

		}
		return b;
	}

}
