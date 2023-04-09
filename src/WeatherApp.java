import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class WeatherApp extends JFrame implements ActionListener {

    private JLabel cityLabel, tempLabel ,descriptionLabel, feelsLikeLabel, HumidityLabel;   // labels for enter city name and getting output temp
    private JTextField cityField; // to take input from user for city name
    private JButton getTempButton; // button to fetch temprature data from openWeather api

    public WeatherApp() { // container for JFrame
        setTitle("Weather App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5,1,2,2)); // we are using BorderLayout as the layout of the frame

        // Create components
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // panel to place the label and button
        cityLabel = new JLabel("Enter city name:");
        cityField = new JTextField(20);
        getTempButton = new JButton("Get Details");

        // adding button and labels to the pannel
        inputPanel.add(cityLabel);
        inputPanel.add(cityField);
        inputPanel.add(getTempButton);

        JPanel outputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // another panel to place the output label
        tempLabel = new JLabel(); // label to output temprature
        outputPanel.add(tempLabel); // output label added to output panel

        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10 )); // panel to get descriptoiin
        descriptionLabel = new JLabel();
        descriptionPanel.add(descriptionLabel);

        JPanel feelsLikePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40,10));
        feelsLikeLabel = new JLabel();
        feelsLikePanel.add(feelsLikeLabel);

        JPanel HumidityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        HumidityLabel = new JLabel();
        HumidityPanel.add(HumidityLabel);

        // Add components to window with berderlayout , we just have two panels that are placed in north and center of the frame
        add(inputPanel);
        add(outputPanel);
        add(feelsLikePanel);
        add(HumidityPanel);
        add(descriptionPanel);

        // Set up event listener
        getTempButton.addActionListener(this);

        // Display the window
        setLocationRelativeTo(null); // where to display the frame window, not relative to anything as null
        setVisible(true); // to display the frame
    }


    // this is used to define the action performed by the button
    public void actionPerformed(ActionEvent e) {
        // Get the city name from the text field
        String city = cityField.getText(); // the text taken from cityField is stored in the city string we created

        // Call the weather API to get the current temperature
        try {
            // API endpoint and API key
            String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=dc41c702477dd1a8c5d5c918cabec124";

            // Create connection to API endpoint
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read response from API endpoint
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the response to get details of different parameters
            double temperature = parseResponse(response.toString());
            String descriptoin = getdescription(response.toString());
            String Humidity = getHumidity(response.toString());
            double feelsLike = feelsLike(response.toString());



            // to display all data of api in the terminal
            //System.out.println("\n\n"+response.toString()+"\n\n");


            // adding value to different labels
            tempLabel.setText(String.format("The temperature in %s is %.1f degrees Celsius.", city, temperature));
            descriptionLabel.setText(String.format("Description: %s",descriptoin));
            feelsLikeLabel.setText(String.format("Feels Like: %.1f",feelsLike));
            HumidityLabel.setText(("Humidity: "+ Humidity +"%"));



        } catch (Exception ex) {
            // Display error message if there was an issue with the API call
            tempLabel.setText("Please enter a valid city name.");
            descriptionLabel.setText(" ");
            HumidityLabel.setText(" ");
            feelsLikeLabel.setText(" ");
        }
    }

    private double parseResponse(String response) {
        // Find the temperature in the response
        int index = response.indexOf("\"temp\":");
        int end = response.indexOf(",", index);
        String tempString = response.substring(index + 7, end);
        double temperature = Double.parseDouble(tempString) - 273.15; // change from kelvin to celsius
        return temperature;
    }

    private double feelsLike(String response){
        int index = response.indexOf("\"feels_like\":");
        int end = response.indexOf(",",index);
        String tempString = response.substring(index + 13, end);
        double feelslike = Double.parseDouble(tempString) - 273.15;
        return feelslike;
    }

    private String getdescription(String response){
        int index = response.indexOf("\"description\":");
        int end = response.indexOf(",",index);
        String descriptionString = response.substring(index + 15,end-1);
        return descriptionString;
    }

    private String getHumidity(String response){
        int index = response.indexOf("\"humidity\":");
        int end = response.indexOf(",",index);
        String humidity = response.substring(index + 11,end);
        return humidity;
    }

    // main function
    public static void main(String[] args) {
        // Create the weather app
        WeatherApp app = new WeatherApp(); // running the frame window
    }
}