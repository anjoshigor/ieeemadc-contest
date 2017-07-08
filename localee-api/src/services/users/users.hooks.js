const savePhoto = require('../../hooks/save-photo');

module.exports = {
  before: {
    all: [],
    find: [],
    get: [],
    create: [savePhoto()],
    update: [],
    patch: [],
    remove: []
  },

  after: {
    all: [],
    find: [],
    get: [],
    create: [],
    update: [],
    patch: [],
    remove: []
  },

  error: {
    all: [],
    find: [],
    get: [],
    create: [],
    update: [],
    patch: [],
    remove: []
  }
};
