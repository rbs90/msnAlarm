import net.sf.jml.*;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.impl.MsnMessengerFactory;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.protocol.MsnSession;

import javax.swing.*;
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
    private static Boolean debug;


    public static void main(final String[] args) {

        for (String arg : args) {
            if (arg.equals("--debug"))
                debug = true;
            else
                text += " " + arg;
        }

        JFrame debugFrame = new JFrame();
        debugFrame.setSize(400, 400);
        final JTextArea debugView = new JTextArea();
        debugFrame.getContentPane().add(new JScrollPane(debugView));

        debugFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (debug) {
            debugFrame.setVisible(true);
        }

        debugView.append("Lese account.txt...");
        BufferedReader reader = null;
        String acc = null;
        String pass = null;
        try {
            reader = new BufferedReader(new FileReader("account.txt"));

            acc = reader.readLine();
            pass = reader.readLine();
            debugView.append("OK");

        } catch (FileNotFoundException e) {
            debugView.append("Datei nicht gefunden!");
        } catch (IOException e) {
            debugView.append("IO Exception:\n" + e);
        }

        debugView.append("\n Verbinde zu MSN...");
        final MsnMessenger messenger = MsnMessengerFactory.createMsnMessenger(acc, pass);
        messenger.setSupportedProtocol(new MsnProtocol[]{MsnProtocol.MSNP12});
        messenger.login();

        //auto close after 10 sec
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //messenger.logout();
                if(debug)
                    debugView.append("\n Nomalerweise: Automatisches Beenden (nach 10 sec.)");
                else
                    System.exit(0);
            }
        }, 10000);


        messenger.addContactListListener(new MsnContactListAdapter() {

            public void contactListInitCompleted(MsnMessenger messenger) {
                //get contacts in allow list
                debugView.append("\n Kontaktliste wurde empfangen");
                MsnContact[] contacts = messenger.getContactList()
                        .getContactsInList(MsnList.AL);




                for (int i = 0; i < contacts.length; i++) {
                    debugView.append("\n Sende Nachricht an: " + contacts[i].getEmail());
                    messenger.sendText(contacts[i].getEmail(), text);
                }
            }
        });
    }
}
