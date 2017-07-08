// Use this hook to manipulate incoming or outgoing data.
// For more information on hooks see: http://docs.feathersjs.com/api/hooks.html
const logger = require('winston');

module.exports = function (options = {}) { // eslint-disable-line no-unused-vars
  return function (hook) {
    // Hooks can either return nothing or a promise
    // that resolves with the `hook` object for asynchronous operations
    logger.info('save-photo hook is running');
    logger.info('hook data:');
    logger.info(hook.data);
    return Promise.resolve(hook);
  };
};
