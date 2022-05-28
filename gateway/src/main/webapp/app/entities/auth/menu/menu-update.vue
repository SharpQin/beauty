<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.authMenu.home.createOrEditLabel"
          data-cy="MenuCreateUpdateHeading"
          v-text="$t('gatewayApp.authMenu.home.createOrEditLabel')"
        >
          Create or edit a Menu
        </h2>
        <div>
          <div class="form-group" v-if="menu.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="menu.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authMenu.name')" for="menu-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="menu-name"
              data-cy="name"
              :class="{ valid: !$v.menu.name.$invalid, invalid: $v.menu.name.$invalid }"
              v-model="$v.menu.name.$model"
              required
            />
            <div v-if="$v.menu.name.$anyDirty && $v.menu.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.menu.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.menu.name.maxLength" v-text="$t('entity.validation.maxlength', { max: 30 })">
                This field cannot be longer than 30 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authMenu.title')" for="menu-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="menu-title"
              data-cy="title"
              :class="{ valid: !$v.menu.title.$invalid, invalid: $v.menu.title.$invalid }"
              v-model="$v.menu.title.$model"
              required
            />
            <div v-if="$v.menu.title.$anyDirty && $v.menu.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.menu.title.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.menu.title.maxLength" v-text="$t('entity.validation.maxlength', { max: 30 })">
                This field cannot be longer than 30 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authMenu.link')" for="menu-link">Link</label>
            <input
              type="text"
              class="form-control"
              name="link"
              id="menu-link"
              data-cy="link"
              :class="{ valid: !$v.menu.link.$invalid, invalid: $v.menu.link.$invalid }"
              v-model="$v.menu.link.$model"
              required
            />
            <div v-if="$v.menu.link.$anyDirty && $v.menu.link.$invalid">
              <small class="form-text text-danger" v-if="!$v.menu.link.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.menu.link.maxLength" v-text="$t('entity.validation.maxlength', { max: 60 })">
                This field cannot be longer than 60 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authMenu.method')" for="menu-method">Method</label>
            <input
              type="text"
              class="form-control"
              name="method"
              id="menu-method"
              data-cy="method"
              :class="{ valid: !$v.menu.method.$invalid, invalid: $v.menu.method.$invalid }"
              v-model="$v.menu.method.$model"
              required
            />
            <div v-if="$v.menu.method.$anyDirty && $v.menu.method.$invalid">
              <small class="form-text text-danger" v-if="!$v.menu.method.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.menu.method.maxLength" v-text="$t('entity.validation.maxlength', { max: 10 })">
                This field cannot be longer than 10 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authMenu.authKey')" for="menu-authKey">Auth Key</label>
            <input
              type="text"
              class="form-control"
              name="authKey"
              id="menu-authKey"
              data-cy="authKey"
              :class="{ valid: !$v.menu.authKey.$invalid, invalid: $v.menu.authKey.$invalid }"
              v-model="$v.menu.authKey.$model"
              required
            />
            <div v-if="$v.menu.authKey.$anyDirty && $v.menu.authKey.$invalid">
              <small class="form-text text-danger" v-if="!$v.menu.authKey.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.menu.authKey.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 30 })"
              >
                This field cannot be longer than 30 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authMenu.showed')" for="menu-showed">Showed</label>
            <input
              type="checkbox"
              class="form-check"
              name="showed"
              id="menu-showed"
              data-cy="showed"
              :class="{ valid: !$v.menu.showed.$invalid, invalid: $v.menu.showed.$invalid }"
              v-model="$v.menu.showed.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authMenu.parent')" for="menu-parent">Parent</label>
            <input
              type="number"
              class="form-control"
              name="parent"
              id="menu-parent"
              data-cy="parent"
              :class="{ valid: !$v.menu.parent.$invalid, invalid: $v.menu.parent.$invalid }"
              v-model.number="$v.menu.parent.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.menu.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./menu-update.component.ts"></script>
