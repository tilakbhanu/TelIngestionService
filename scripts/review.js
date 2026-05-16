const fs = require('fs');
const Anthropic = require('@anthropic-ai/sdk');
const { execSync } = require('child_process');

const client = new Anthropic({
    apiKey: process.env.ANTHROPIC_API_KEY
});

const prNumber = process.env.PR_NUMBER;

const diff = fs.readFileSync('pr.diff', 'utf8');
const rules = fs.readFileSync('CLAUDE.md', 'utf8');

async function run() {

    const response = await client.messages.create({
        model: "claude-sonnet-4",
        max_tokens: 4000,
        messages: [{
            role: "user",
            content: `
Review this PR.

RULES:
${rules}

PR DIFF:
${diff}

Provide:
1. Critical issues
2. Security concerns
3. Performance concerns
4. Refactoring suggestions
5. Final rating
`
        }]
    });

    const reviewText = response.content[0].text;

    console.log(reviewText);

    // safer approach
    fs.writeFileSync('review.txt', reviewText);

    execSync(`
      gh pr comment ${prNumber} --body-file review.txt
    `);

    console.log('PR comment added successfully');
}

run().catch(err => {
    console.error(err);
    process.exit(1);
});