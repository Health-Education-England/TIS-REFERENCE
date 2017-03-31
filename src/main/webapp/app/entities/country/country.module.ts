import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	CountryService,
	CountryPopupService,
	CountryComponent,
	CountryDetailComponent,
	CountryDialogComponent,
	CountryPopupComponent,
	CountryDeletePopupComponent,
	CountryDeleteDialogComponent,
	countryRoute,
	countryPopupRoute,
	CountryResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...countryRoute,
	...countryPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		CountryComponent,
		CountryDetailComponent,
		CountryDialogComponent,
		CountryDeleteDialogComponent,
		CountryPopupComponent,
		CountryDeletePopupComponent,
	],
	entryComponents: [
		CountryComponent,
		CountryDialogComponent,
		CountryPopupComponent,
		CountryDeleteDialogComponent,
		CountryDeletePopupComponent,
	],
	providers: [
		CountryService,
		CountryPopupService,
		CountryResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceCountryModule {
}
