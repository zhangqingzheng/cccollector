/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.stylesSet = [
		{ name: '小标题', element: 'span', attributes: { 'class': 'subhead' } },
		{ name: '图片说明', element: 'span', attributes: { 'class': 'caption' } },
		{ name: '导航按钮', element: 'span', attributes: { 'class': 'navButton' } },
		{ name: '购买按钮', element: 'span', attributes: { 'class': 'buyButton' } },
		{ name: '标题', element: 'div', attributes: { 'class': 'title' } },
		{ name: '作者', element: 'span', attributes: { 'class': 'author' } },
		{ name: '来源', element: 'span', attributes: { 'class': 'source' } },
		{ name: '发布时间', element: 'span', attributes: { 'class': 'publishTime' } },
	];
};
