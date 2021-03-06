package edu.fontbonne.rick.fontbonnecampus;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CafeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe);

        new ConnectTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cafe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ConnectTask extends AsyncTask<Void, Void, Pair<Document, Date>>
    {
        protected Pair<Document, Date> doInBackground(Void... arg0)
        {
            HttpURLConnection connection = null;
            Document xmlDoc = null;
            String weekNumber = null;
            Date weekStartDate = null;

            try
            {
                URL url = new URL("http://www.primetechconsult.com/fontbonnecampusapp/weeknumber.txt");
                connection = (HttpURLConnection)url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                weekNumber = reader.readLine();
                SimpleDateFormat weekDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
                weekStartDate = weekDateFormatter.parse(reader.readLine());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (connection != null)
                    connection.disconnect();
            }

            connection = null;

            try
            {
                URL url = new URL("http://www.primetechconsult.com/fontbonnecampusapp/menu" + (Integer.parseInt(weekNumber) % 5) + ".xml");
                connection = (HttpURLConnection)url.openConnection();
                InputStream is = connection.getInputStream();
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                xmlDoc = documentBuilder.parse(is);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (connection != null)
                    connection.disconnect();
            }

            Pair menuPair = new Pair(xmlDoc, weekStartDate);

            return menuPair;
        }

        protected void onPostExecute(Pair<Document, Date> result) {

            String[] menu = new String[15];

            ArrayList<Node> days = new ArrayList<>();
            NodeList menuNodes = result.a.getElementsByTagName("menu").item(0).getChildNodes();
            for(int a = 0; a < menuNodes.getLength(); a++)
            {
                if(menuNodes.item(a).getNodeType() == Node.ELEMENT_NODE)
                    days.add(menuNodes.item(a));
            }
            for (int i = 0; i < days.size(); i++)
            {
                ArrayList<Node> meals = new ArrayList<>();
                NodeList daysNodes = days.get(i).getChildNodes();
                for(int b = 0; b < daysNodes.getLength(); b++)
                {
                    if(daysNodes.item(b).getNodeType() == Node.ELEMENT_NODE)
                        meals.add(daysNodes.item(b));
                }
                for (int j = 0; j < meals.size(); j++)
                {
                    ArrayList<Node> dishes = new ArrayList<>();
                    NodeList mealsNodes = meals.get(j).getChildNodes();
                    for(int c = 0; c < mealsNodes.getLength(); c++)
                    {
                        if(mealsNodes.item(c).getNodeType() == Node.ELEMENT_NODE)
                            dishes.add(mealsNodes.item(c));
                    }
                    for (int k = 0; k < dishes.size(); k++)
                    {
                        if (menu[(i*3)+j] != null)
                            menu[(i*3)+j] = menu[(i*3)+j] + "\n" + dishes.get(k).getTextContent();
                        else
                            menu[(i*3)+j] = dishes.get(k).getTextContent();
                    }
                }
            }

            TextView menuTitle = (TextView) findViewById(R.id.menuTitle);

            TextView menuText1 = (TextView) findViewById(R.id.menuText1);
            TextView menuText2 = (TextView) findViewById(R.id.menuText2);
            TextView menuText3 = (TextView) findViewById(R.id.menuText3);
            TextView menuText4 = (TextView) findViewById(R.id.menuText4);
            TextView menuText5 = (TextView) findViewById(R.id.menuText5);
            TextView menuText6 = (TextView) findViewById(R.id.menuText6);
            TextView menuText7 = (TextView) findViewById(R.id.menuText7);
            TextView menuText8 = (TextView) findViewById(R.id.menuText8);
            TextView menuText9 = (TextView) findViewById(R.id.menuText9);
            TextView menuText10 = (TextView) findViewById(R.id.menuText10);
            TextView menuText11 = (TextView) findViewById(R.id.menuText11);
            TextView menuText12 = (TextView) findViewById(R.id.menuText12);
            TextView menuText13 = (TextView) findViewById(R.id.menuText13);
            TextView menuText14 = (TextView) findViewById(R.id.menuText14);
            TextView menuText15 = (TextView) findViewById(R.id.menuText15);

            SimpleDateFormat resultFormatter = new SimpleDateFormat("MMM dd, yyyy");

            menuTitle.setText("Menu for Week of\n    " + resultFormatter.format(result.b));

            menuText1.setText(menu[0]);
            menuText2.setText(menu[1]);
            menuText3.setText(menu[2]);
            menuText4.setText(menu[3]);
            menuText5.setText(menu[4]);
            menuText6.setText(menu[5]);
            menuText7.setText(menu[6]);
            menuText8.setText(menu[7]);
            menuText9.setText(menu[8]);
            menuText10.setText(menu[9]);
            menuText11.setText(menu[10]);
            menuText12.setText(menu[11]);
            menuText13.setText(menu[12]);
            menuText14.setText(menu[13]);
            menuText15.setText(menu[14]);
        }
    }

    public class Pair<A,B>
    {
        public final A a;
        public final B b;

        public Pair(A a, B b)
        {
            this.a = a;
            this.b = b;
        }
    }
}
