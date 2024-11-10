export default {
    extends: ['@commitlint/config-conventional'],
    // https://commitlint.js.org/#/reference-rules
    rules: {
        'body-max-line-length': [2, 'always', 150],
    },
    ignores: [(commit) => ['[skip ci]', '[skip ci]', '[no ci]'].some(it => commit.includes(it)) ||
        /Create PR for #\d+/.test(commit)],
};
