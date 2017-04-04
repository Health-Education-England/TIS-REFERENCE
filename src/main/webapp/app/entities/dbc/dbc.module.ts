import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	DBCService,
	DBCPopupService,
	DBCComponent,
	DBCDetailComponent,
	DBCDialogComponent,
	DBCPopupComponent,
	DBCDeletePopupComponent,
	DBCDeleteDialogComponent,
	dBCRoute,
	dBCPopupRoute
} from "./";

let ENTITY_STATES = [
	...dBCRoute,
	...dBCPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		DBCComponent,
		DBCDetailComponent,
		DBCDialogComponent,
		DBCDeleteDialogComponent,
		DBCPopupComponent,
		DBCDeletePopupComponent,
	],
	entryComponents: [
		DBCComponent,
		DBCDialogComponent,
		DBCPopupComponent,
		DBCDeleteDialogComponent,
		DBCDeletePopupComponent,
	],
	providers: [
		DBCService,
		DBCPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceDBCModule {
}
