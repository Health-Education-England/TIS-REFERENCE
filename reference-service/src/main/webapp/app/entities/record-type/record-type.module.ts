import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	RecordTypeService,
	RecordTypePopupService,
	RecordTypeComponent,
	RecordTypeDetailComponent,
	RecordTypeDialogComponent,
	RecordTypePopupComponent,
	RecordTypeDeletePopupComponent,
	RecordTypeDeleteDialogComponent,
	recordTypeRoute,
	recordTypePopupRoute
} from "./";

let ENTITY_STATES = [
	...recordTypeRoute,
	...recordTypePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		RecordTypeComponent,
		RecordTypeDetailComponent,
		RecordTypeDialogComponent,
		RecordTypeDeleteDialogComponent,
		RecordTypePopupComponent,
		RecordTypeDeletePopupComponent,
	],
	entryComponents: [
		RecordTypeComponent,
		RecordTypeDialogComponent,
		RecordTypePopupComponent,
		RecordTypeDeleteDialogComponent,
		RecordTypeDeletePopupComponent,
	],
	providers: [
		RecordTypeService,
		RecordTypePopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceRecordTypeModule {
}
