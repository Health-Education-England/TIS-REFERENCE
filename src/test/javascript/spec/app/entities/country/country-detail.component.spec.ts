import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {CountryDetailComponent} from "../../../../../../main/webapp/app/entities/country/country-detail.component";
import {CountryService} from "../../../../../../main/webapp/app/entities/country/country.service";
import {Country} from "../../../../../../main/webapp/app/entities/country/country.model";

describe('Component Tests', () => {

	describe('Country Management Detail Component', () => {
		let comp: CountryDetailComponent;
		let fixture: ComponentFixture<CountryDetailComponent>;
		let service: CountryService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [CountryDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					CountryService
				]
			}).overrideComponent(CountryDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(CountryDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(CountryService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Country(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.country).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
