module.exports = {
    debug: true,
    tagFormat: '${version}',
    branches: [
        'master',
    ],
    plugins: [
        ['@semantic-release/commit-analyzer', {
            preset: "conventionalcommits",
            releaseRules: [
                {breaking: true, release: 'major'},
                {revert: true, release: 'patch'},
                // Conventional Commits
                {type: 'feat', release: 'minor'},
                {type: 'fix', release: 'patch'},
                {type: 'perf', release: 'patch'},
                {type: 'chore', release: 'patch'},
                // {type: 'docs', release: 'patch'},
                {type: 'build', release: 'patch'},
                {type: 'ci', release: 'patch'},
            ]
        }],
        ['@semantic-release/release-notes-generator', {
            preset: "conventionalcommits",
            presetConfig: {
                "types": [
                    {type: 'feat', section: '⭐ New Features'},
                    {type: 'fix', section: '🐞 Bug Fixes'},
                    {type: 'perf', section: '📈 Performance Improvements'},
                    {type: 'revert', section: '🔙 Reverts'},
                    {type: 'docs', section: '📔 Documentation'},
                    {type: 'style', section: 'Styles', hidden: true},
                    {type: 'chore', section: 'Miscellaneous Chores', hidden: true},
                    {type: 'refactor', section: 'Code Refactoring', hidden: true},
                    {type: 'test', section: 'Tests', hidden: true},
                    {type: 'build', section: '🔨 Build System'},
                    {type: 'ci', section: '⚙️ Continuous Integration'}
                ]
            }
        }],
        ['@semantic-release/github', {draftRelease: true}],
        '@semantic-release/changelog',
        ['@semantic-release/git', {assets: ['CHANGELOG.md']}]
    ]
}
