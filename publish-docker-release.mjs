#!/usr/bin/env zx
import 'zx/globals';
import {confirm, input} from '@inquirer/prompts';
import reader from 'properties-reader';

const sourceFilePath = ".release.properties";
const sourceFileEncoding = "utf-8";

async function main() {
    await $`touch ${sourceFilePath}`;

    const props = reader(sourceFilePath, sourceFileEncoding, {writer: {saveSections: false}});
    const configValues = {
        imageName: await getOrSave(props, "IMAGE_NAME",
            async () => await input({message: "What is your docker image name?"})),
        publishLatest: await getOrSave(props, "IS_PUBLISH_LATEST",
            async () => await confirm({message: "Update latest tag on each publication?"})),
        publishVersion: (await $`git describe --tags --abbrev=0`).stdout.trim(),
    };

    const publishTags = [
        `${configValues.imageName}:${configValues.publishVersion}`
    ];
    if (configValues.publishLatest) {
        publishTags.push(`${configValues.imageName}:latest`);
    }

    console.info(`
Image name: ${configValues.imageName}
Target version: ${configValues.publishVersion}
Published tags: ${publishTags.join(" | ")}
    `);

    const answer = await confirm({message: 'Continue?', default: false});
    if (!answer) process.exit(0);

    await $({verbose: true})`./gradlew clean build`;

    await $({verbose: true})`docker build ${publishTags.flatMap(it => ["-t", it])} .`;

    for (const tag of publishTags) {
        await $({verbose: true})`docker push ${tag}`;
    }
}

async function getOrSave(props, key, alternative) {
    let value = props.get(key);
    if (null === value) {
        value = await alternative();
        props.set(key, value);
        await props.save(sourceFilePath);
    }
    return value;
}

try {
    await main();
} catch (error) {
    if (error instanceof Error && error.name === 'ExitPromptError') {
        console.log('👋 until next time!');
    } else {
        throw error;
    }
}
