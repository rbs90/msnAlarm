import net.sf.jml.*;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.impl.MsnMessengerFactory;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.protocol.MsnSession;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 06.03.12
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class Starter {
    public static void main(final String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("account.txt"));

        String acc = reader.readLine();
        String pass = reader.readLine();

        MsnMessenger messenger = MsnMessengerFactory.createMsnMessenger(acc, pass);
        messenger.setSupportedProtocol(new MsnProtocol[] {MsnProtocol.MSNP12});
        messenger.login();

        messenger.addContactListListener(new MsnContactListAdapter(){
            public void contactListInitCompleted(MsnMessenger messenger) {
                //get contacts in allow list
                MsnContact[] contacts = messenger.getContactList().getContactsInList(MsnList.AL);

                //System.out.println("Contact count = " + contacts.length);
                for (int i = 0; i < contacts.length; i++) {
                    //System.out.println(contacts[i].getFriendlyName() + " - " + contacts[i].getEmail());
                    messenger.sendText(contacts[i].getEmail(), args[0]);
                }
            }
        });
    }
}
