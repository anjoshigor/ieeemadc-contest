// Use this hook to manipulate incoming or outgoing data.
// For more information on hooks see: http://docs.feathersjs.com/api/hooks.html
const logger = require('winston');

module.exports = function (options = {}) { // eslint-disable-line no-unused-vars
  return function (hook) {
    const event = hook.result;
    const user_id = event.createdBy._id;

    logger.info('event_id: ' + event._id);
    logger.info('user_id: ' + user_id);

    return hook.app.service('users').update(user_id, {
      $push: {
        eventsCreated: {
          event: {
            _id: event._id,
            name: event.name,
            date: event.date,
            photoUrl: event.photoUrl
          }
        }
      }
    }).then(data => {
      logger.info('updated user ' + data);
      return Promise.resolve(hook);
    });
  };
};
