import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	SettledService,
	SettledPopupService,
	SettledComponent,
	SettledDetailComponent,
	SettledDialogComponent,
	SettledPopupComponent,
	SettledDeletePopupComponent,
	SettledDeleteDialogComponent,
	settledRoute,
	settledPopupRoute
} from "./";

let ENTITY_STATES = [
	...settledRoute,
	...settledPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		SettledComponent,
		SettledDetailComponent,
		SettledDialogComponent,
		SettledDeleteDialogComponent,
		SettledPopupComponent,
		SettledDeletePopupComponent,
	],
	entryComponents: [
		SettledComponent,
		SettledDialogComponent,
		SettledPopupComponent,
		SettledDeleteDialogComponent,
		SettledDeletePopupComponent,
	],
	providers: [
		SettledService,
		SettledPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceSettledModule {
}
