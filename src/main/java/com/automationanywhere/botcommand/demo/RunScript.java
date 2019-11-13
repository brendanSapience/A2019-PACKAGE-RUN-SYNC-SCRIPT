/*
 * Copyright (c) 2019 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */
/**
 *
 */
package com.automationanywhere.botcommand.demo;

import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

/**
 * @author Bren Sapience
 *
 */
@BotCommand
@CommandPkg(label="Run Script", name="Run Script", description="Run Script Synchronously", icon="pkg.svg",
		node_label="Run Script {{scriptPath}}",
		return_type=STRING, return_label="Assign the output to variable", return_required=false)

public class RunScript {

	private static final Messages MESSAGES = MessagesFactory.getMessages("com.automationanywhere.botcommand.demo.messages");

	@Execute
	public StringValue action(
			@Idx(index = "1", type = TEXT) @Pkg(label = "Script Path") @NotEmpty String ScriptPath,
			@Idx(index = "2", type = TEXT) @Pkg(label = "parameter") @NotEmpty String ScriptParameter
	) {

		if ("".equals(ScriptPath.trim()))
			throw new BotCommandException(MESSAGES.getString("emptyInputString", "scriptPath"));

		String OUTPUT = "";
		List<String> list = new ArrayList<String>();
		list.add(ScriptPath);
		list.add(ScriptParameter);
		ProcessBuilder processBuilder = new ProcessBuilder(list);


		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line);
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				return new StringValue(output.toString());
				//System.out.println(output);
				//System.exit(0);
			} else {
				//abnormal...
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

}
