import net.sf.jml.*;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.impl.MsnMessengerFactory;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.protocol.MsnSession;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 06.03.12
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class Starter {
    
    private static String text = "";

    
    public static void main(final String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("account.txt"));

        String acc = reader.readLine();
        String pass = reader.readLine();

        final MsnMessenger messenger = MsnMessengerFactory.createMsnMessenger(acc, pass);
        messenger.setSupportedProtocol(new MsnProtocol[] {MsnProtocol.MSNP12});
        messenger.login();

        for(String arg : args){
            text += " " + arg;
        }

        //auto close after 10 sec
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //messenger.logout();
                System.exit(0);
            }
        }, 10000);
        
        messenger.addContactListListener(new MsnContactListAdapter() {

            public void contactListInitCompleted(MsnMessenger messenger) {
                //get contacts in allow list
                MsnContact[] contacts = messenger.getContactList()
                        .getContactsInList(MsnList.AL);

                
                for (int i = 0; i < contacts.length; i++) {
                    messenger.sendText(contacts[i].getEmail(), text);
                }
            }

        });
    }
}
