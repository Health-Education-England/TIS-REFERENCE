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
import {MaritalStatusDetailComponent} from "../../../../../../main/webapp/app/entities/marital-status/marital-status-detail.component";
import {MaritalStatusService} from "../../../../../../main/webapp/app/entities/marital-status/marital-status.service";
import {MaritalStatus} from "../../../../../../main/webapp/app/entities/marital-status/marital-status.model";

describe('Component Tests', () => {

	describe('MaritalStatus Management Detail Component', () => {
		let comp: MaritalStatusDetailComponent;
		let fixture: ComponentFixture<MaritalStatusDetailComponent>;
		let service: MaritalStatusService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [MaritalStatusDetailComponent],
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
					MaritalStatusService
				]
			}).overrideComponent(MaritalStatusDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(MaritalStatusDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(MaritalStatusService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new MaritalStatus(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.maritalStatus).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
