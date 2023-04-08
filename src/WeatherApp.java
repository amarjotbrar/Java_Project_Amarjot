import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class WeatherApp extends JFrame implements ActionListener {

    private JLabel cityLabel, tempLabel;
    private JTextField cityField;
    private JButton getTempButton;

    public WeatherApp() {
        // Set up the window
        setTitle("Weather App");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        // Create components
        cityLabel = new JLabel("Enter city name:");
        cityField = new JTextField();
        tempLabel = new JLabel();
        getTempButton = new JButton("Get temperature");

        // Add components to window
        add(cityLabel);
        add(cityField);
        add(tempLabel);
        add(getTempButton);

        // Set up event listener
        getTempButton.addActionListener(this);

        // Display the window
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // Get the city name from the text field
        String city = cityField.getText();

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

            // Parse the response to get the temperature
            double temperature = parseResponse(response.toString());

            // Display the temperature to the user
            tempLabel.setText(String.format("The temperature in %s is %.1f degrees Celsius.", city, temperature));

        } catch (Exception ex) {
            // Display error message if there was an issue with the API call
            tempLabel.setText("Error: " + ex.getMessage());
        }
    }

    private double parseResponse(String response) {
        // Find the temperature in the response
        int index = response.indexOf("\"temp\":");
        int end = response.indexOf(",", index);
        String tempString = response.substring(index + 7, end);
        double temperature = Double.parseDouble(tempString) - 273.15;
        return temperature;
    }

    public static void main(String[] args) {
        // Create the weather app
        WeatherApp app = new WeatherApp();
    }
}
