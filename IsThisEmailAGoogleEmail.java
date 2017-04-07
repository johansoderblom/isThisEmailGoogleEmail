import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

/**
 * Created by johan on 2017-04-06.
 */
public class IsThisEmailAGoogleEmail {

    // Main to run from commandline

    public static void main(String args[]) throws NamingException {
        if (args.length < 1) {
            System.err.println("What email do you want to check?");
            System.exit(0);
        }
        IsThisEmailAGoogleEmail isThisEmailAGoogleEmail = new IsThisEmailAGoogleEmail();
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i] + ": " + isThisEmailAGoogleEmail.isGoogleMail(args[i]));
        }

    }

    public boolean isGoogleMail(String email) throws NamingException {
        String hostName;
        if (isValidEmailAddress(email)) {
            System.out.println("Is valid Email: " + isValidEmailAddress(email));
            hostName = extractHostnameFromEmail(email);
            if (hostName.equalsIgnoreCase("gmail.com")) {
                return true;
            }
            try {
                System.out.println("Is google adress: " + isGoogleMailserver(hostName));
                return isGoogleMailserver(hostName);
            } catch (NamingException e) {
                throw new NamingException("I couldnt handle this name input. Is it realy a hostname?");
            }
        }
        return false;
    }

    // Helper Methodes private ===>
    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isGoogleMailserver(String hostName) throws NamingException {
        Hashtable env = new Hashtable();

        env.put("java.naming.factory.initial",
                "com.sun.jndi.dns.DnsContextFactory");
        DirContext domainName = new InitialDirContext(env);

        Attributes attrs = domainName.getAttributes(hostName, new String[]{"MX"});

        Attribute attr = attrs.get("MX");
        if (attr == null) return false;

        for (int i = 0; i < attr.size(); i++) {
            String emailServer = attr.get(i).toString();
            String[] splitString = emailServer.split("(\\.)");
            for (int j = 0; j < splitString.length; j++) {
                if (splitString[j].equalsIgnoreCase("google") || splitString[j].equalsIgnoreCase("googlemail")) {
                    return true;
                }
            }
        }
        return false;
    }

    private String extractHostnameFromEmail(String email) {
        String domain;
        domain = email.split("@", 2)[1];
        System.out.println("Domain: " + domain);
        return domain;
    }
}
