package dsergeyev.example.models.converters;

import java.awt.Color;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ColorConverter implements AttributeConverter<Color, String> {

	private static final String SEPARATOR = " ";

	@Override
	public String convertToDatabaseColumn(Color color) {
		if(color == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(color.getRed()).append(SEPARATOR).append(color.getGreen()).append(SEPARATOR).append(color.getBlue())
				.append(SEPARATOR).append(color.getAlpha());
		return sb.toString();
	}

	@Override
	public Color convertToEntityAttribute(String colorString) {
		if(colorString == null) 
			return null;
		
		String[] rgb = colorString.split(SEPARATOR);
		return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]),
				Integer.parseInt(rgb[3]));
	}
}
